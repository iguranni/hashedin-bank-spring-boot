package com.hashedin.hashedinbank.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Company")
@ToString
@Table(name = "COMPANY", uniqueConstraints = {@UniqueConstraint(columnNames = "COMPANY_CD", name = "XAK1COMPANY")})
@DynamicInsert
public class Company extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPANY_ID", nullable = false, updatable = false)
    private Integer companyId;

    @Column(name = "COMPANY_NME", nullable = false, length = 50)
    private String companyName;

    @Column(name = "COMPANY_CD", nullable = false, length = 50, updatable = false)
    private String companyCode;

    @Email
    @Column(name = "COMPANY_EMAIL", nullable = false, length = 50, updatable = false)
    private String companyEmail;

    @Column(name = "COMPANY_DESC", nullable = false)
    private String companyDescription;

    @Column(name = "EFFECTIVE_START_DT", nullable = false, updatable = false)
    private LocalDateTime effectiveStartDate;

    @Column(name = "EFFECTIVE_END_DT", nullable = false)
    @ColumnDefault(value = "9999-12-31 23:59:59")
    private LocalDateTime effectiveEndDate;

    @Column(name = "IS_ACTIVE_FLG", nullable = false)
    @ColumnDefault(value = "true")
    private Boolean isActiveFlag;
}