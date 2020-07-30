package com.proximity.labs.qcounter.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;



import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import com.proximity.labs.qcounter.annotation.CurrentUser;
import com.proximity.labs.qcounter.data.dto.response.ApiResponse;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.event.OnUserLogoutSuccessEvent;
import com.proximity.labs.qcounter.service.AuthService;
import com.proximity.labs.qcounter.service.UserService;

import org.apache.log4j.Logger;
import org.apache.tomcat.jni.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.activation.MimetypesFileTypeMap;

@Api(value = "User Rest API", description = "Defines endpoints for the logged in user.this enpoints served functions related to user management. It's secured by default")
@RestController("/user")
public class UserController {

  private static final Logger logger = Logger.getLogger(UserController.class);

  private final AuthService authService;

  private final UserService userService;

  private final ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  public UserController(final AuthService authService, final UserService userService,
      final ApplicationEventPublisher applicationEventPublisher) {
    this.authService = authService;
    this.userService = userService;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @GetMapping("/test")
  public String test(@RequestParam final String name) {
    return "";
  }

  public static String hashData(final String data) throws NoSuchAlgorithmException, IOException {
    final MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(data.getBytes());
    final byte[] b = md.digest();
    final StringBuffer sb = new StringBuffer();
    for (final byte b1 : b) {
      sb.append(Integer.toHexString(b1 & 0xff).toString());
    }
    return sb.toString();
  }

  /**
   * This methods retrieve .png/ /.jpg from client with max size of 500kb and
   * perform file operations to save it with file name user_id + name doc
   * by @chathil
   * 
   * @param customUserDetails
   * @return String
   */
  @ApiOperation(value = "When the user signup theres no options to set profile picture. so this is the route to set it later.")
  @PatchMapping("/ava")
  public String ava(@CurrentUser final User customUserDetails, @NonNull @RequestParam("file") final MultipartFile file)
      throws IllegalStateException, IOException, NoSuchAlgorithmException, URISyntaxException {

    final Path baseDir = Paths.get("src/main/resources/profile/");
    final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
    final long size = file.getSize() / 1024;
    final long maxSize = 500;

    final File f = new File(file.getOriginalFilename());
    final String mimetype = new MimetypesFileTypeMap().getContentType(f);
    final String type = mimetype.split("/")[0];
    if (type.equals("image")) {
      if (size <= maxSize) {
        final File directory = new File(baseDir.toAbsolutePath().toString());
        final File[] files = directory.listFiles();
        for (final File fl : files) {
          if (fl.getName().contains(hashData(customUserDetails.getId() + customUserDetails.getName()))) {
            fl.delete();
            break;
          }
        }
        file.transferTo(new File(baseDir.toAbsolutePath().toString() + "/"
            + hashData(customUserDetails.getId() + customUserDetails.getName()) + "." + extension));
      } else {
        System.out.println("Image size cannot exceed 500kb");
      }
    } else {
      System.out.println("It's NOT an image");
    }
    return null;
  }

  /**
   * Log the user out from the app/device. Release the refresh token associated
   * with the user device. doc by @chathil
   * 
   * @param customUserDetails
   * @param deviceToken
   * @return ResponseEntity
   */
  @GetMapping("/signout")
  @ApiOperation(value = "Logs the specified user device and clears the refresh tokens associated with it")
  public ResponseEntity logout(@CurrentUser User customUserDetails, @RequestParam("device_token") String deviceToken) {
    userService.logout(deviceToken);
    Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
    OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(customUserDetails.getEmail(),
        credentials.toString());
    applicationEventPublisher.publishEvent(logoutSuccessEvent);
    return ResponseEntity.ok(new ApiResponse(true, "Log out successful"));
  }

}