package com.proximity.labs.qcounter.controllers;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class UserController {
  private static final Logger logger = Logger.getLogger(UserController.class);

  private final AuthService authService;

  private final UserService userService;

  private final ApplicationEventPublisher applicationEventPublisher;
  
  @Autowired
  public UserController(AuthService authService, UserService userService, ApplicationEventPublisher applicationEventPublisher) {
      this.authService = authService;
      this.userService = userService;
      this.applicationEventPublisher = applicationEventPublisher;
  }

  @GetMapping("/test")
  public String test(@RequestParam String name) {
    return "";
  }

  @PatchMapping("/ava")
  public String ava() {
    // @Yusup
    // TODO upload file pindah disini
    return null;
  }

  /**
     * Log the user out from the app/device. Release the refresh token associated with the
     * user device.
     */
    @GetMapping("/signout")
    @ApiOperation(value = "Logs the specified user device and clears the refresh tokens associated with it")
    public ResponseEntity logout(@CurrentUser User customUserDetails, @RequestParam("device_token") String deviceToken) {
        userService.logout(deviceToken);
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(customUserDetails.getEmail(), credentials.toString());
        applicationEventPublisher.publishEvent(logoutSuccessEvent);
        return ResponseEntity.ok(new ApiResponse(true, "Log out successful"));
    }


}