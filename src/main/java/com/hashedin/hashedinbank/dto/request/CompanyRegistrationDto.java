package com.hashedin.hashedinbank.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class CompanyRegistrationDto {

    @NotBlank
    private String companyName;
    @NotBlank
    private String companyCode;
    @Email
    @NotBlank
    private String companyEmail;
    @NotBlank
    private String companyDescription;
    private LocalDateTime effectiveStartDate;
    private LocalDateTime effectiveEndDate;

    @JsonCreator
    public CompanyRegistrationDto(
            @JsonProperty("companyName") String companyName,
            @JsonProperty("companyCode") String companyCode,
            @JsonProperty("companyEmail") String companyEmail,
            @JsonProperty("companyDescription") String companyDescription,
            @JsonProperty("effectiveStartDate") LocalDateTime effectiveStartDate,
            @JsonProperty("effectiveEndDate") LocalDateTime effectiveEndDate) {
        this.companyName = companyName;
        this.companyCode = companyCode;
        this.companyEmail = companyEmail;
        this.companyDescription = companyDescription;
        this.effectiveStartDate = effectiveStartDate;
        this.effectiveEndDate = effectiveEndDate;
    }
}
