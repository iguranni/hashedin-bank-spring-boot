package com.hashedin.hashedinbank.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@ToString
@SuperBuilder
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "USER", uniqueConstraints = {@UniqueConstraint(columnNames = "EMAIL", name = "XAK1USER")})
public class User extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "FIRST_NME", nullable = false, length = 50)
    private String firstName;

    @Column(name = "LAST_NME", nullable = false, length = 50)
    private String lastName;

    @Email
    @Column(name = "EMAIL", nullable = false, length = 50, updatable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Size(min = 10, max = 10)
    @Pattern(regexp = "(^$|\\d{10})")
    @Column(name = "PHONE", nullable = false)
    private String phoneNo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID"))
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "COMPANY_ID")
    private Company companyId;

    @Column(name = "IS_ACTIVE_FLG", nullable = false)
    @ColumnDefault(value = "true")
    private boolean isActiveFlag;

    @Column(name = "IS_APPROVED_FLG", nullable = false)
    @ColumnDefault(value = "false")
    private boolean isApprovedFlag;

    @Column(name = "REMARKS")
    private String remarks;

    @OneToMany(mappedBy = "cardHolderId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<CreditCard> cards = new HashSet<>();
}
