package com.proximity.labs.qcounter.controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.proximity.labs.qcounter.dto.GuestRequest;
import com.proximity.labs.qcounter.dto.SignupResponse;
import com.proximity.labs.qcounter.models.user.UserEntity;
import com.proximity.labs.qcounter.models.user.UserDevice;
import com.proximity.labs.qcounter.models.user.UserDeviceRepository;
import com.proximity.labs.qcounter.models.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserDeviceRepository userDeviceRepository;
	
	@PostMapping("/auth/guest")

	public @ResponseBody SignupResponse guestSignup (
		@RequestParam("device_token") String deviceToken,
		@RequestParam("ip_address") String ipAddress,
		@RequestParam String name
		) {
		UserEntity newUser = new UserEntity(name, null, null, ipAddress);
		UserEntity savedUser = userRepository.save(newUser);
		userDeviceRepository.save(new UserDevice(deviceToken, savedUser));
		return new SignupResponse(
			savedUser.getId(),
			deviceToken,
			getJWTToken(savedUser.getName()),
			savedUser.getIpAddress(),
			savedUser.getName(),
			savedUser.getEmail(),
			savedUser.getProfilePicture(),
			savedUser.getLocation(),
			savedUser.getAccountType(),
			savedUser.getProfileCompletion()
		);
	}

	@PostMapping("/auth/signup")
	public @ResponseBody SignupResponse signup (
		@RequestParam("device_token") String deviceToken,
		@RequestParam("ip_address") String ipAddress,
		@RequestParam String name,
		@RequestParam String email,
		@RequestParam String password
	) {
		UserEntity newUser = new UserEntity(name, email, password, ipAddress);
		UserEntity savedUser = userRepository.save(newUser);
		userDeviceRepository.save(new UserDevice(deviceToken, savedUser));
		return new SignupResponse(
			savedUser.getId(),
			deviceToken,
			getJWTToken(savedUser.getName()),
			savedUser.getIpAddress(),
			savedUser.getName(),
			savedUser.getEmail(),
			savedUser.getProfilePicture(),
			savedUser.getLocation(),
			savedUser.getAccountType(),
			savedUser.getProfileCompletion()
		);
	}

	@GetMapping("/auth/signin")
	public SignupResponse signin(
		@RequestParam("device_token") String deviceToken,
		@RequestParam("ip_address") String ipAddress,
		@RequestParam String email,
		@RequestParam String password
		) {
		return null;
	}

	private String getJWTToken(String username) {
		String secretKey = "my-super-secret-key";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("q-counter-jwt")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
	
}