package com.hashedin.hashedinbank.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hashedin.hashedinbank.dto.request.*;
import com.hashedin.hashedinbank.entities.Company;
import com.hashedin.hashedinbank.entities.CreditCard;
import com.hashedin.hashedinbank.entities.Role;
import com.hashedin.hashedinbank.entities.User;
import com.hashedin.hashedinbank.enums.ERole;
import com.hashedin.hashedinbank.services.impl.UserDetailsImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

public class ConstructTestObjectUtil {

    private ConstructTestObjectUtil() {
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }


    public static Company constructCompany() {
        return Company.builder()
                .companyCode("INFY001")
                .companyName("INFOSYS")
                .companyDescription("INFY_MNC")
                .companyEmail("random@infy.com")
                .effectiveStartDate(LocalDateTime.of(1999, 1, 1, 0, 0))
                .build();
    }

    public static CompanyRegistrationDto constructCompanyRegistrationDto() {
        return CompanyRegistrationDto.builder()
                .companyCode("DEL002")
                .companyName("DELOITTE")
                .companyDescription("Consulting MNC")
                .companyEmail("test1@deloitte.com")
                .effectiveStartDate(LocalDateTime.of(1999, 12, 31, 0, 0))
                .effectiveEndDate(LocalDateTime.of(9999, 12, 31, 23, 59))
                .build();
    }

    public static CompanyUpdateDto constructCompanyUpdateDto() {
        return CompanyUpdateDto.builder()
                .companyId(1)
                .companyName("DELOITTE")
                .companyDescription("Consulting MNC")
                .effectiveEndDate(LocalDateTime.of(2022, 12, 31, 23, 59))
                .build();
    }

    public static Role constructRole() {
        return Role.builder()
                .roleId(1)
                .roleName(ERole.ROLE_USER)
                .build();
    }

    public static User constructUser() {
        return User.builder()
                .userId(1L)
                .firstName("iram")
                .lastName("guranni")
                .phoneNo("9035959092")
                .email("iram@walmart.com")
                .password("test@123")
                .companyId(constructCompany())
                .roles(Set.of(constructRole()))
                .isActiveFlag(true)
                .isApprovedFlag(true)
                .build();
    }

    public static UserRegistrationDto constructUserRegistrationDto() {
        return UserRegistrationDto.builder()
                .companyId(1)
                .firstName("iram")
                .lastName("guranni")
                .phoneNo("9035959092")
                .email("iram@walmart.com")
                .password("test@123")
                .roles(Set.of("ROLE_USER"))
                .remarks("test")
                .build();
    }

    public static UserRegistrationDto constructUserRegistrationDto_InvalidRole() {
        return UserRegistrationDto.builder()
                .companyId(1)
                .firstName("iram")
                .lastName("guranni")
                .phoneNo("9035959092")
                .email("iram@walmart.com")
                .password("test@123")
                .roles(Set.of(""))
                .remarks("test")
                .build();
    }

    public static UserRegistrationDto constructUserRegistrationDto_AdminRole() {
        return UserRegistrationDto.builder()
                .companyId(1)
                .firstName("iram")
                .lastName("guranni")
                .phoneNo("9035959092")
                .email("iram@walmart.com")
                .password("test@123")
                .roles(Set.of("ROLE_PROGRAM_ADMIN"))
                .remarks("test")
                .build();
    }

    public static UserUpdateDto constructUserUpdateDto() {
        return UserUpdateDto.builder()
                .userId(1L)
                .firstName("iram")
                .lastName("guranni")
                .phoneNo("9035959092")
                .password("test@123")
                .roles(Set.of("ROLE_PROGRAM_ADMIN"))
                .remarks("testing update scenario")
                .build();
    }

    public static CreditCard constructCreditCard() {
        return CreditCard
                .builder()
                .creditCardId(1L)
                .creditCardNumber("1234567891011121")
                .cardHolderId(constructUser())
                .issuedDate(LocalDate.of(2022, 12, 31))
                .expiredDate(LocalDate.of(2023,12,31))
                .build();
    }

    public static LoginRequestDto constructLoginRequestDto() {
        return LoginRequestDto.builder()
                .username("test@gmail.com")
                .password("test@123")
                .build();
    }

    public static UserDetailsImpl constructUserDetails() {
        return UserDetailsImpl.build(constructUser());
    }
}
