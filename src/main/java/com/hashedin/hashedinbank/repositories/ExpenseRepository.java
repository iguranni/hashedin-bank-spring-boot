package com.hashedin.hashedinbank.repositories;

import com.hashedin.hashedinbank.entities.Expense;
import com.hashedin.hashedinbank.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT E FROM Expense E INNER JOIN User U ON E.userId = U INNER JOIN Company C ON U.companyId = C WHERE C.companyId = :companyId AND E.createdAt >= :expenseExportLimit AND U.isActiveFlag = :isActiveFlg ORDER BY E.userId.userId")
    List<Expense> findMonthlyExpenseByCompany(Integer companyId, Boolean isActiveFlg, LocalDateTime expenseExportLimit);
}
