package com.hashedin.hashedinbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}