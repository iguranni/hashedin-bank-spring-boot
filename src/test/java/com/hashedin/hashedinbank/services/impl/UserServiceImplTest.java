package com.hashedin.hashedinbank.services.impl;

import com.hashedin.hashedinbank.HashedinBankApplicationTests;
import com.hashedin.hashedinbank.entities.Company;
import com.hashedin.hashedinbank.entities.User;
import com.hashedin.hashedinbank.enums.ERole;
import com.hashedin.hashedinbank.exception.CompanyNotFoundException;
import com.hashedin.hashedinbank.exception.RoleNotFoundException;
import com.hashedin.hashedinbank.exception.UserAlreadyExistsException;
import com.hashedin.hashedinbank.repositories.CompanyRepository;
import com.hashedin.hashedinbank.repositories.RoleRepository;
import com.hashedin.hashedinbank.repositories.UserRepository;
import com.hashedin.hashedinbank.utils.ConstructTestObjectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.hashedin.hashedinbank.utils.ConstructTestObjectUtil.constructUserRegistrationDto_InvalidRole;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserServiceImplTest extends HashedinBankApplicationTests {

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
        Mockito.when(userRepository.save(any(User.class))).thenReturn(ConstructTestObjectUtil.constructUser());
        Mockito.when(roleRepository.findByRoleName(any(ERole.class))).thenReturn(Optional.ofNullable(ConstructTestObjectUtil.constructRole()));
        Mockito.when(companyRepository.findByCompanyIdAndIsActiveFlag(anyInt(), anyBoolean())).thenReturn(Optional.ofNullable(ConstructTestObjectUtil.constructCompany()));
        doNothing().when(applicationEventPublisher).publishEvent(any(User.class));
        user.setApprovedFlag(true);
        User user1 = userServiceImpl.createUser(ConstructTestObjectUtil.constructUserRegistrationDto());
        Assertions.assertNotNull(user1);
    }

    @Test
    void createUser_whenThrowsException() {
        User user = ConstructTestObjectUtil.constructUser();
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(true);

        UserAlreadyExistsException exception = Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userServiceImpl.createUser(ConstructTestObjectUtil.constructUserRegistrationDto()));

        Assertions.assertEquals("User already exists with email id : iram@walmart.com", exception.getMessage());
    }

    @Test
    void createUser_whenThrowsRoleNotFoundException() {
        User user = ConstructTestObjectUtil.constructUser();
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(ConstructTestObjectUtil.constructUser());

        RoleNotFoundException exception = Assertions.assertThrows(RoleNotFoundException.class,
                () -> userServiceImpl.createUser(ConstructTestObjectUtil.constructUserRegistrationDto()));

        Assertions.assertEquals("Error: Role is not found", exception.getMessage());
    }

    @Test
    void createUser_whenThrowsCompanyNotFoundException() {
        User user = ConstructTestObjectUtil.constructUser();
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(roleRepository.findByRoleName(any(ERole.class))).thenReturn(Optional.ofNullable(ConstructTestObjectUtil.constructRole()));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(ConstructTestObjectUtil.constructUser());

        CompanyNotFoundException exception = Assertions.assertThrows(CompanyNotFoundException.class,
                () -> userServiceImpl.createUser(ConstructTestObjectUtil.constructUserRegistrationDto()));

        Assertions.assertEquals("Active Company not found with id : 1", exception.getMessage());
    }

    @Test
    void createUser_whenInvalidRole() {
        User user = ConstructTestObjectUtil.constructUser();
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(ConstructTestObjectUtil.constructUser());
        Mockito.when(roleRepository.findByRoleName(any(ERole.class))).thenReturn(Optional.ofNullable(ConstructTestObjectUtil.constructRole()));
        Mockito.when(companyRepository.findByCompanyIdAndIsActiveFlag(anyInt(), anyBoolean())).thenReturn(Optional.ofNullable(ConstructTestObjectUtil.constructCompany()));
        doNothing().when(applicationEventPublisher).publishEvent(any(User.class));
        user.setApprovedFlag(true);

        RoleNotFoundException exception = Assertions.assertThrows(RoleNotFoundException.class,
                () -> userServiceImpl.createUser(ConstructTestObjectUtil.constructUserRegistrationDto_AdminRole()));

        Assertions.assertEquals("Role not allowed or invalid role", exception.getMessage());
    }

    @Test
    void createAdmin() {
        User user = ConstructTestObjectUtil.constructUser();
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(roleRepository.findByRoleName(any(ERole.class))).thenReturn(Optional.ofNullable(ConstructTestObjectUtil.constructRole()));
        Mockito.when(companyRepository.findByCompanyIdAndIsActiveFlag(anyInt(), anyBoolean())).thenReturn(Optional.ofNullable(ConstructTestObjectUtil.constructCompany()));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(ConstructTestObjectUtil.constructUser());
        User user1 = userServiceImpl.createAdmin(ConstructTestObjectUtil.constructUserRegistrationDto_AdminRole());
        Assertions.assertNotNull(user1);
    }

    @Test
    void createAdmin_whenInvalidRole() {
        User user = ConstructTestObjectUtil.constructUser();
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(ConstructTestObjectUtil.constructUser());
        Mockito.when(roleRepository.findByRoleName(any(ERole.class))).thenReturn(Optional.ofNullable(ConstructTestObjectUtil.constructRole()));
        Mockito.when(companyRepository.findByCompanyIdAndIsActiveFlag(anyInt(), anyBoolean())).thenReturn(Optional.ofNullable(ConstructTestObjectUtil.constructCompany()));
        doNothing().when(applicationEventPublisher).publishEvent(any(User.class));
        user.setApprovedFlag(true);

        RoleNotFoundException exception = Assertions.assertThrows(RoleNotFoundException.class,
                () -> userServiceImpl.createAdmin(ConstructTestObjectUtil.constructUserRegistrationDto()));

        Assertions.assertEquals("Role not allowed..", exception.getMessage());
    }

    @Test
    void updateUser() {
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(ConstructTestObjectUtil.constructUser()));
        Mockito.when(userRepository.findByUserIdAndCompanyIdAndIsActiveFlag(anyLong(), any(Company.class), anyBoolean())).thenReturn(ConstructTestObjectUtil.constructUser());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(ConstructTestObjectUtil.constructUser());
        User user1 = userServiceImpl.updateUser(ConstructTestObjectUtil.constructUserUpdateDto(), ConstructTestObjectUtil.constructUser().getEmail());
        Assertions.assertNotNull(user1);
    }

    @Test
    void deleteUser() {
        User user = ConstructTestObjectUtil.constructUser();
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.findByUserIdAndCompanyIdAndIsActiveFlag(anyLong(), any(Company.class), anyBoolean())).thenReturn(user);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        user.setActiveFlag(false);
        userServiceImpl.deleteUser(user.getUserId(), user.getEmail());
    }

    @Test
    void getAllUserByCompany() {
        User user = ConstructTestObjectUtil.constructUser();
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.findAllByCompanyIdAndIsActiveFlag(any(Company.class), anyBoolean(), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(user)));
        userServiceImpl.getAllUserByCompany(user.getUserId());
    }

    @Test
    void getAllUsers() {
        User user = ConstructTestObjectUtil.constructUser();
        Mockito.when(userRepository.findAllByIsActiveFlag(anyBoolean(), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(user)));
        userServiceImpl.getAllUsers();
    }

    @Test
    void approveUser() {
        User user = ConstructTestObjectUtil.constructUser();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        user.setApprovedFlag(true);
        userServiceImpl.approveUser(user.getEmail());
    }
}