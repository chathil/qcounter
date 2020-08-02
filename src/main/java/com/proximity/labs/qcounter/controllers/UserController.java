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