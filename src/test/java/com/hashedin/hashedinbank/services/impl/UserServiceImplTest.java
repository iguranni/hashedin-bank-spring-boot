package com.hashedin.hashedinbank.services.impl;

import com.hashedin.hashedinbank.entities.User;
import com.hashedin.hashedinbank.repositories.CompanyRepository;
import com.hashedin.hashedinbank.repositories.RoleRepository;
import com.hashedin.hashedinbank.repositories.UserRepository;
import com.hashedin.hashedinbank.utils.ConstructTestObjectUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    CompanyRepository companyRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    UserServiceImpl userServiceImpl;


    @Test
    void createUser() {
        User user = ConstructTestObjectUtil.constructUser();
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(false);
    }

    @Test
    void createAdmin() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getAllUserByCompany() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void approveUser() {
    }
}