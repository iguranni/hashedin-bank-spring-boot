package com.hashedin.hashedinbank.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserRegistrationDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @Size(min = 10, max = 10)
    @Pattern(regexp = "(^$|\\d{10})")
    @NotBlank
    private String phoneNo;
    private Set<String> roles;
    private Integer companyId;
    private String remarks;
}
