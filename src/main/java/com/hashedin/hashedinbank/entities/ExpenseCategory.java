package com.hashedin.hashedinbank.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "EXPENSE_CATEGORY", uniqueConstraints = {@UniqueConstraint(columnNames = "EXPENSE_CD", name = "XAK1EXPENSE_CATEGORY")})
public class ExpenseCategory extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXPENSE_CATEGORY_ID", nullable = false, updatable = false)
    private Long expenseCategoryId;

    @Column(name = "EXPENSE_CATEGORY_NME", nullable = false)
    private String expenseCategoryName;

    @Column(name = "EXPENSE_CD", nullable = false, updatable = false)
    private String expenseCode;
}