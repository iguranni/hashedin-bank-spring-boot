package com.hashedin.hashedinbank.controllers;

import com.hashedin.hashedinbank.common.ApiResponse;
import com.hashedin.hashedinbank.constants.SecurityConstants;
import com.hashedin.hashedinbank.dto.request.LoginRequestDto;
import com.hashedin.hashedinbank.dto.request.UserRegistrationDto;
import com.hashedin.hashedinbank.entities.User;
import com.hashedin.hashedinbank.services.UserService;
import com.hashedin.hashedinbank.services.impl.UserDetailsImpl;
import com.hashedin.hashedinbank.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtUtils jwtUtils;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, UserService userService,
                           JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/user")
    public ResponseEntity<ApiResponse<UserDetailsImpl>> authenticateUser(@RequestBody LoginRequestDto loginRequestDto) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

        final HttpHeaders httpHeaders = new HttpHeaders();
        UserDetailsImpl userDetails = null;
        if (null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Generating Auth token...");
            String jwtToken = jwtUtils.generateJwtToken(authentication);
            httpHeaders.add(SecurityConstants.JWT_HEADER, jwtToken);
            userDetails = (UserDetailsImpl) authentication.getPrincipal();
        }

        return new ResponseEntity<>(new ApiResponse<>("Successfully authenticated the user",
                null, userDetails), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerAdmin(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        log.info("Inside UserController_registerAdmin..");
        User user = userService.createAdmin(userRegistrationDto);
        log.info("Exiting UserController_registerAdmin..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully registered " + userRegistrationDto.getRoles() + " with HashedIn Bank for company Id : " + userRegistrationDto.getCompanyId(),
                null, user), HttpStatus.CREATED);
    }
}
