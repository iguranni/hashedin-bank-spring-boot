package com.hashedin.hashedinbank.services;

import com.hashedin.hashedinbank.dto.request.UserRegistrationDto;
import com.hashedin.hashedinbank.dto.request.UserUpdateDto;
import com.hashedin.hashedinbank.entities.User;
import org.springframework.data.domain.Page;

public interface UserService {
    User createUser(UserRegistrationDto userRegistrationDto);

    User createAdmin(UserRegistrationDto userRegistrationDto);

    User updateUser(UserUpdateDto userUpdateDto, String email);

    void deleteUser(Long userId, String email);

    Page<User> getAllUserByCompany(Long userId);

    Page<User> getAllUsers();

    void approveUser(String email);
}
