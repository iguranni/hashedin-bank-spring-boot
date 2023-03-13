package com.hashedin.hashedinbank.repositories;

import com.hashedin.hashedinbank.entities.Role;
import com.hashedin.hashedinbank.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(ERole role);
}