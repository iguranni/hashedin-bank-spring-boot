package com.hashedin.hashedinbank.repositories;

import com.hashedin.hashedinbank.entities.Company;
import com.hashedin.hashedinbank.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findAllByCompanyIdAndIsActiveFlag(Company companyId, boolean isActiveFlag, Pageable pageable);

    Page<User> findAllByIsActiveFlag(boolean isActiveFlag, Pageable pageable);

    User findByUserIdAndCompanyIdAndIsActiveFlag(Long userId, Company companyId, boolean isActiveFlag);

    @Query("SELECT U FROM User U INNER JOIN CreditCard CC ON CC.cardHolderId = U WHERE CC.isActiveFlag = :isActiveFlag AND U.email = :email")
    Optional<User> findByEmailAndActiveCreditCard(String email, boolean isActiveFlag);
}