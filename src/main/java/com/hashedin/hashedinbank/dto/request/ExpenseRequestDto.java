package com.hashedin.hashedinbank.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpenseRequestDto {
    @JsonAlias("BILL_ID")
    private Long billId;

    @NotBlank
    @JsonAlias("EXPENSE_CD")
    private String expenseCode;

    @NotBlank
    @JsonAlias("TOTAL_EXPENDITURE")
    private String totalExpenditure;

    @NotBlank
    @JsonAlias("RECEIPT_DATE")
    private String receiptDate;

    @NotBlank
    @JsonAlias("COMMENTS")
    private String comments;
}