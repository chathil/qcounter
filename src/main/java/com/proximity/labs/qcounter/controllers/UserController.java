package com.proximity.labs.qcounter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

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

}