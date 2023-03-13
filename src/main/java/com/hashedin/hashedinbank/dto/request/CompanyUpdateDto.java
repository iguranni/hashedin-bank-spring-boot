package com.hashedin.hashedinbank.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CompanyUpdateDto {

    private Integer companyId;
    private String companyName;
    private String companyDescription;
    private LocalDateTime effectiveEndDate;
}