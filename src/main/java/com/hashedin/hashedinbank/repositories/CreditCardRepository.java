package com.hashedin.hashedinbank.repositories;

import com.hashedin.hashedinbank.entities.CreditCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    Page<CreditCard> findAllByIsActiveFlag(boolean isActiveFlag, Pageable pageable);
}
