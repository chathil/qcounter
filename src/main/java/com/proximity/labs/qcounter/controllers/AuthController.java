package com.proximity.labs.qcounter.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.proximity.labs.qcounter.data.dto.response.SignInResponse;
import com.proximity.labs.qcounter.data.dto.response.SignupResponse;
import com.proximity.labs.qcounter.data.models.user.AccountType;
import com.proximity.labs.qcounter.data.models.user.UserDeviceEntity;
import com.proximity.labs.qcounter.data.models.user.UserDeviceRepository;
import com.proximity.labs.qcounter.data.models.user.UserEntity;
import com.proximity.labs.qcounter.data.models.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController // annotation processor
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserDeviceRepository userDeviceRepository;

	@PostMapping("/auth/guest")
	public @ResponseBody SignupResponse guestSignup(@RequestParam("device_token") String deviceToken,
			@RequestParam("ip_address") String ipAddress, @RequestParam String name) {
		UserEntity newUser = new UserEntity(name, null, null, ipAddress);
		UserEntity savedUser = userRepository.save(newUser);
		userDeviceRepository.save(new UserDeviceEntity(deviceToken, savedUser));
		return new SignupResponse(savedUser.getId(), savedUser.hexId(), deviceToken, getJWTToken(savedUser.getName(), deviceToken),
				savedUser.getIpAddress(), savedUser.getName(), savedUser.getEmail(), null, savedUser.getLocation(),
				savedUser.getAccountType(), savedUser.getProfileCompletion());
	}

	@PostMapping("/auth/signup")
	public @ResponseBody SignupResponse signup(@RequestParam("device_token") String deviceToken,
			@RequestParam("ip_address") String ipAddress, @RequestParam String name, @RequestParam String email,
			@RequestParam String password) {
		UserEntity newUser = new UserEntity(name, email, password, ipAddress);
		newUser.setAccountType(AccountType.SIGNED);
		UserEntity savedUser = userRepository.save(newUser);
		userDeviceRepository.save(new UserDeviceEntity(deviceToken, savedUser));
		return new SignupResponse(savedUser.getId(), savedUser.hexId(), deviceToken, getJWTToken(savedUser.getName(), deviceToken),
				savedUser.getIpAddress(), savedUser.getName(), savedUser.getEmail(), null, // get profile pic with hash
				savedUser.getLocation(), savedUser.getAccountType(), savedUser.getProfileCompletion());
	}

	@GetMapping("/auth/signin")
	public SignInResponse signin(@RequestParam("device_token") String deviceToken,
			@NonNull @RequestParam("ip_address") String ipAddress, @RequestParam String email,
			@RequestParam String password) {
		final UserEntity user = userRepository.findFirstByEmailAndPassword(email, password);
		userDeviceRepository.save(new UserDeviceEntity(deviceToken, user));
		System.out.println(user.getEmail() + " " + user.getName());
		return new SignInResponse(user.getId(), user.hexId(), deviceToken, getJWTToken(user.getName(), deviceToken),
				user.getIpAddress(), user.getName(), user.getEmail(), null, // get profile pic with hash
				user.getLocation(), user.getAccountType(), user.getProfileCompletion());
	}

	@GetMapping("/auth/signout")
	public HashMap<String, Boolean> signout() {
			
		String token = SecurityContextHolder.getContext().getAuthentication()
                    .getDetails().toString();

		
		Long result = userDeviceRepository.deleteFirstByDeviceToken(token);
		System.out.println(token + " " + result);

		HashMap<String, Boolean> success = new HashMap<String, Boolean>();
		success.put("OK", true);

		return success;
	}

	private String getJWTToken(String username, String deviceToken) {
		String secretKey = "my-super-secret-key";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

		String token = Jwts.builder().setId("q-counter-jwt").setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.claim("device_token", deviceToken).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 24 * 600000))
				.signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
		return "Bearer " + token;
	}
}