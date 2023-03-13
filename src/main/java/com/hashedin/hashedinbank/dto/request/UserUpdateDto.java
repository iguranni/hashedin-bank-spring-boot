package com.hashedin.hashedinbank.dto.request;

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
public class UserUpdateDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String password;
    @Size(min = 10, max = 10)
    @Pattern(regexp = "(^$|\\d{10})")
    private String phoneNo;
    private Set<String> roles;
    private String remarks;
}