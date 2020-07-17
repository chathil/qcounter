package com.proximity.labs.qcounter.controllers;


import com.proximity.labs.qcounter.dto.GuestRequest;
import com.proximity.labs.qcounter.dto.SignupResponse;
import com.proximity.labs.qcounter.models.user.User;
import com.proximity.labs.qcounter.models.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@Autowired
	private UserRepository userRepository;


	// @GetMapping("/greeting")
	// public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
	// 	return new Greeting(counter.incrementAndGet(), String.format(template, name));
	// }

//   @GetMapping(path="/all")
//   public @ResponseBody Iterable<User> getAllUsers() {
//     // This returns a JSON or XML with the users
//     return userRepository.findAll();
//   }
	
	@PostMapping("/user/signup/guest")
	public @ResponseBody SignupResponse guestSignup (
		@RequestParam("device_token") String deviceToken,
		@RequestParam("ip_address") String ipAddress,
		@RequestParam String name
		) {
		User newUser = new User(name, null, null, ipAddress);
		User savedUser = userRepository.save(newUser);
		return new SignupResponse(
			savedUser.getId(),
			deviceToken,
			"this is access token",
			savedUser.getIpAddress(),
			savedUser.getName(),
			savedUser.getEmail(),
			savedUser.getProfilePicture(),
			savedUser.getLocation(),
			savedUser.getAccountType(),
			savedUser.getProfileCompletion()
		);
	}
}