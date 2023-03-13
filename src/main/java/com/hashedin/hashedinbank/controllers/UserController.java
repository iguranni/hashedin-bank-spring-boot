package com.hashedin.hashedinbank.controllers;

import com.hashedin.hashedinbank.common.ApiResponse;
import com.hashedin.hashedinbank.dto.request.UserRegistrationDto;
import com.hashedin.hashedinbank.dto.request.UserUpdateDto;
import com.hashedin.hashedinbank.entities.User;
import com.hashedin.hashedinbank.services.UserService;
import com.hashedin.hashedinbank.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PROGRAM_ADMIN')")
    public ResponseEntity<ApiResponse<User>> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        log.info("Inside UserController_registerUser..");
        User user = userService.createUser(userRegistrationDto);
        log.info("Exiting UserController_registerUser..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully registered user with HashedIn Bank for company Id : " + userRegistrationDto.getCompanyId(),
                null, user), HttpStatus.CREATED);
    }

    @PutMapping("/approve")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PROGRAM_ADMIN')")
    public ResponseEntity<ApiResponse<String>> approveUser(@RequestParam String email) {
        log.info("Inside UserController_approveUser..");
        userService.approveUser(email);
        log.info("Exiting UserController_approveUser..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully approved user with HashedIn Bank with email : " + email,
                null, null), HttpStatus.ACCEPTED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('PROGRAM_ADMIN')")
    public ResponseEntity<ApiResponse<User>> updateUser(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        log.info("Inside UserController_updateUser..");
        String email = jwtUtils.getUsernameFromToken(bearerToken);
        User user = userService.updateUser(userUpdateDto, email);
        log.info("Exiting UserController_updateUser..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully updated user details with HashedIn Bank for user id : " + userUpdateDto.getUserId(),
                null, user), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('PROGRAM_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteUser(@RequestHeader("Authorization") String bearerToken, @RequestParam Long userId) {
        log.info("Inside UserController_deleteUser..");
        String email = jwtUtils.getUsernameFromToken(bearerToken);
        userService.deleteUser(userId,email);
        log.info("Exiting UserController_deleteUser..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully deleted user from HashedIn Bank with user id : " + userId,
                null, null), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all-by-company")
    @PreAuthorize("hasRole('PROGRAM_ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> fetchAllUsersByCompany(@RequestParam Long userId) {
        log.info("Inside UserController_fetchAllUserByCompany..");
        Page<User> allUsersList = userService.getAllUserByCompany(userId);
        log.info("Exiting UserController_fetchAllUserByCompany..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully fetched all users from HashedIn Bank associated with company : " + allUsersList.getContent().get(0).getCompanyId().getCompanyName(),
                allUsersList.getTotalElements(), allUsersList.getContent()), HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> fetchAllUsers() {
        log.info("Inside UserController_fetchAllUser..");
        Page<User> allUsersList = userService.getAllUsers();
        log.info("Exiting UserController_fetchAllUser..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully fetched all users from HashedIn Bank : ",
                allUsersList.getTotalElements(), allUsersList.getContent()), HttpStatus.OK);
    }
}