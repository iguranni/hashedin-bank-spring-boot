package com.hashedin.hashedinbank.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExpenseService {
    void uploadExpense(MultipartFile multipartFile, String username) throws IOException;

    void calculateMonthlyExpenseByCompany();
}
