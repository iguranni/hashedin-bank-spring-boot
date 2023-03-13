package com.hashedin.hashedinbank.repositories;

import com.hashedin.hashedinbank.entities.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    Optional<ExpenseCategory> findByExpenseCode(String expenseCode);
}
