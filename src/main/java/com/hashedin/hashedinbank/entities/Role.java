package com.hashedin.hashedinbank.entities;

import com.hashedin.hashedinbank.enums.ERole;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID", nullable = false, updatable = false)
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_NME", nullable = false, length = 50)
    private ERole roleName;
}