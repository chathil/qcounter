package com.proximity.labs.qcounter.controllers;

<<<<<<< HEAD
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.bind.DatatypeConverter;

import com.proximity.labs.qcounter.data.models.user.UserEntity;
import com.proximity.labs.qcounter.data.models.user.UserRepository;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
=======
import com.proximity.labs.qcounter.annotation.CurrentUser;
import com.proximity.labs.qcounter.data.dto.response.ApiResponse;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.event.OnUserLogoutSuccessEvent;
import com.proximity.labs.qcounter.service.AuthService;
import com.proximity.labs.qcounter.service.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
>>>>>>> 71e7a8d65e468e4b1a118bd12da1cd6e0cef2e62
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;

@RestController
public class UserController {
  // @Autowired
  
  private static final Logger logger = Logger.getLogger(UserController.class);

  private final AuthService authService;

  private final UserService userService;

  private final ApplicationEventPublisher applicationEventPublisher;
  
  @Autowired
  // private UserRepository userRepository;
  public UserController(final AuthService authService, final UserService userService, final ApplicationEventPublisher applicationEventPublisher) {
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

  @PatchMapping("/ava")
  public String ava(@NonNull @RequestParam("file") final MultipartFile file, @RequestParam final String email,
      @RequestParam final String password) throws IllegalStateException, IOException, NoSuchAlgorithmException {
    // final Path root = Paths.get("uploads");

    // final Path root = Paths.get("uploads");

    // try {
    // Files.createDirectory(root);

    // } catch (IOException e) {
    // throw new RuntimeException("Could not initialize folder for upload!");
    // }

    final String baseDir = "D:/Project/Springboot/uploads/";
    // Optional<User> user = userService.findByEmail(email);
        final UserEntity user = userRepository.findFirstByEmailAndPassword(email, password);
    final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
    final long size = file.getSize() / 1024;
    final long maxSize = 500;

    final File f = new File(file.getOriginalFilename());
    final String mimetype = new MimetypesFileTypeMap().getContentType(f);
    final String type = mimetype.split("/")[0];
    // cek file yang di upload gambar atau bukan
    if (type.equals("image")) {
      // cek ukuran file
      if (size <= maxSize) {
        file.transferTo(new File(baseDir + hashData(user.getId() + user.getName()) + "." + extension));
      } else {
        System.out.println("Image size cannot exceed 500kb");
      }
    } else {
      System.out.println("It's NOT an image");
    }
    return null;
  }

  /**
     * Log the user out from the app/device. Release the refresh token associated with the
     * user device.
     */
    @GetMapping("/signout")
    @ApiOperation(value = "Logs the specified user device and clears the refresh tokens associated with it")
    public ResponseEntity logout(@CurrentUser final User customUserDetails, @RequestParam("device_token") final String deviceToken) {
        userService.logout(deviceToken);
        final Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

        final OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(customUserDetails.getEmail(), credentials.toString());
        applicationEventPublisher.publishEvent(logoutSuccessEvent);
        return ResponseEntity.ok(new ApiResponse(true, "Log out successful"));
    }


}