package com.hashedin.hashedinbank.services.impl;

import com.hashedin.hashedinbank.dto.request.UserRegistrationDto;
import com.hashedin.hashedinbank.dto.request.UserUpdateDto;
import com.hashedin.hashedinbank.entities.Company;
import com.hashedin.hashedinbank.entities.Role;
import com.hashedin.hashedinbank.entities.User;
import com.hashedin.hashedinbank.enums.ERole;
import com.hashedin.hashedinbank.exception.CompanyNotFoundException;
import com.hashedin.hashedinbank.exception.RoleNotFoundException;
import com.hashedin.hashedinbank.exception.UserAlreadyExistsException;
import com.hashedin.hashedinbank.exception.UserNotFoundException;
import com.hashedin.hashedinbank.repositories.CompanyRepository;
import com.hashedin.hashedinbank.repositories.RoleRepository;
import com.hashedin.hashedinbank.repositories.UserRepository;
import com.hashedin.hashedinbank.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static com.hashedin.hashedinbank.constants.AppConstants.PAGE_LIMIT;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           CompanyRepository companyRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public User createUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email id : " + userRegistrationDto.getEmail());
        }
        User user = adaptUserRegistrationDto(userRegistrationDto,
                validateRoleForUser(userRegistrationDto.getRoles()), fetchExistingCompany(userRegistrationDto.getCompanyId()));
        user.setApprovedFlag(true);
        user = userRepository.save(user);
        applicationEventPublisher.publishEvent(user);
        return user;
    }

    @Override
    @Transactional
    public User createAdmin(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email id : " + userRegistrationDto.getEmail());
        }
        Company existingCompany = null;
        Set<Role> roles = validateRoleForAdmin(userRegistrationDto.getRoles(), userRegistrationDto.getCompanyId(), existingCompany);
        return userRepository.save(adaptUserRegistrationDto(userRegistrationDto, roles, existingCompany));
    }

    @Override
    @Transactional
    public User updateUser(UserUpdateDto userUpdateDto, String email) {
        User existingUser = userRepository.findByEmail(email)
                .map(user -> userRepository.findByUserIdAndCompanyIdAndIsActiveFlag(userUpdateDto.getUserId(), user.getCompanyId(), Boolean.TRUE))
                .orElseThrow(() -> new UserNotFoundException("Active User not found with userId : " + userUpdateDto.getUserId() + " associated with your company"));

        return userRepository.save(updateUserDetails(userUpdateDto, existingUser));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId, String email) {
        User existingUser = userRepository.findByEmail(email)
                .map(user -> userRepository.findByUserIdAndCompanyIdAndIsActiveFlag(userId, user.getCompanyId(), Boolean.TRUE))
                .orElseThrow(() -> new UserNotFoundException("Active User not found with userId : " + userId + " associated with your company"));

        existingUser.setActiveFlag(false);
        userRepository.save(existingUser);
    }

    /**
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public Page<User> getAllUserByCompany(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id : " + userId));

        log.info("company id: {}", user.getCompanyId().getCompanyId());

        return userRepository.findAllByCompanyIdAndIsActiveFlag(user.getCompanyId(), Boolean.TRUE, PageRequest.of(0, PAGE_LIMIT));
    }

    @Override
    @Transactional
    public Page<User> getAllUsers() {
        return userRepository.findAllByIsActiveFlag(Boolean.TRUE, PageRequest.of(0, PAGE_LIMIT));
    }

    @Override
    public void approveUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));
        user.setApprovedFlag(true);
        userRepository.save(user);
    }


    private User adaptUserRegistrationDto(UserRegistrationDto userRegistrationDto, Set<Role> roles, Company existingCompany) {
        return User.builder()
                .firstName(userRegistrationDto.getFirstName())
                .lastName(userRegistrationDto.getLastName())
                .email(userRegistrationDto.getEmail())
                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                .phoneNo(userRegistrationDto.getPhoneNo())
                .roles(roles)
                .companyId(existingCompany)
                .isActiveFlag(Boolean.TRUE)
                .remarks(userRegistrationDto.getRemarks())
                .build();
    }

    private Set<Role> validateRoleForAdmin(Set<String> strRoles, Integer companyId, Company existingCompany) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role superRole = roleRepository.findByRoleName(ERole.ROLE_SUPER_ADMIN)
                    .orElseThrow(getRoleNotFoundExceptionSupplier());
            roles.add(superRole);
        } else {
            for (String role : strRoles) {
                switch (role) {
                    case "ROLE_USER" -> throw new RoleNotFoundException("Role not allowed..");
                    case "ROLE_PROGRAM_ADMIN" -> {
                        Role programAdminRole = roleRepository.findByRoleName(ERole.ROLE_PROGRAM_ADMIN)
                                .orElseThrow(getRoleNotFoundExceptionSupplier());
                        roles.add(programAdminRole);
                        existingCompany = fetchExistingCompany(companyId);
                    }
                    case "ROLE_ADMIN" -> {
                        Role adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(getRoleNotFoundExceptionSupplier());
                        roles.add(adminRole);
                    }
                    default -> {
                        Role superRole = roleRepository.findByRoleName(ERole.ROLE_SUPER_ADMIN)
                                .orElseThrow(getRoleNotFoundExceptionSupplier());
                        roles.add(superRole);
                    }
                }
            }
        }
        return roles;
    }

    private Set<Role> validateRoleForUser(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (!strRoles.contains(ERole.ROLE_USER.name())) {
            throw new RoleNotFoundException("Role not allowed or invalid role");
        } else {
            Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(getRoleNotFoundExceptionSupplier());
            roles.add(userRole);
        }
        return roles;
    }

    private Company fetchExistingCompany(Integer companyId) {
        if (companyId != null) {
            return companyRepository.findByCompanyIdAndIsActiveFlag(companyId, Boolean.TRUE)
                    .orElseThrow(() -> new CompanyNotFoundException("Active Company not found with id : " + companyId));
        } else {
            throw new CompanyNotFoundException("Please input registered company id");
        }
    }

    private Supplier<RoleNotFoundException> getRoleNotFoundExceptionSupplier() {
        return () -> new RoleNotFoundException("Error: Role is not found");
    }

    private User updateUserDetails(UserUpdateDto userUpdateDto, User existingUser) {
        existingUser.setFirstName(userUpdateDto.getFirstName());
        existingUser.setLastName(userUpdateDto.getLastName());
        existingUser.setPassword(userUpdateDto.getPassword());
        existingUser.setPhoneNo(userUpdateDto.getPhoneNo());
        existingUser.setRemarks(userUpdateDto.getRemarks());

        return existingUser;
    }
}