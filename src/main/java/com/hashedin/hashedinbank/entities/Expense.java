package com.hashedin.hashedinbank.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "EXPENSE")
public class Expense extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXPENSE_ID", nullable = false, updatable = false)
    private Long expenseId;

    @Column(name = "BILL_ID", nullable = false)
    private Long billId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false, updatable = false)
    private User userId;

    @ManyToOne
    @JoinColumn(name = "EXPENSE_CATEGORY_ID", nullable = false, updatable = false)
    private ExpenseCategory expenseCategoryId;

    @Column(name = "TOTAL_EXPENDITURE", nullable = false)
    private BigDecimal totalExpenditure;

    @Column(name = "RECEIPT_DATE", nullable = false)
    private LocalDate receiptDate;

    @Column(name = "COMMENTS", nullable = false)
    private String comments;
}
