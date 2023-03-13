package com.hashedin.hashedinbank.controllers;

import com.hashedin.hashedinbank.common.ApiResponse;
import com.hashedin.hashedinbank.services.ExpenseService;
import com.hashedin.hashedinbank.utils.JobScheduler;
import com.hashedin.hashedinbank.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final JobScheduler jobScheduler;
    private final JwtUtils jwtUtils;

    @Autowired
    public ExpenseController(ExpenseService expenseService,
                             JwtUtils jwtUtils, JobScheduler jobScheduler) {
        this.expenseService = expenseService;
        this.jwtUtils = jwtUtils;
        this.jobScheduler = jobScheduler;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<String>> uploadExpenseSheet(@RequestHeader("Authorization") String bearerToken, @RequestParam("file") MultipartFile uploadFile) throws Exception {
        log.info("Inside ExpenseController_uploadExpenseSheet..");
        String email = jwtUtils.getUsernameFromToken(bearerToken);
        expenseService.uploadExpense(uploadFile, email);
        log.info("Exiting ExpenseController_uploadExpenseSheet..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully uploaded expense details", null, null), HttpStatus.CREATED);
    }

    @PostMapping("/monthly-expense")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> calculateMonthlyExpense() throws Exception {
        log.info("Inside ExpenseController_calculateMonthlyExpense..");
        expenseService.calculateMonthlyExpenseByCompany();
        log.info("Exiting ExpenseController_calculateMonthlyExpense..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully generated monthly expense file", null, null), HttpStatus.CREATED);
    }

    @PostMapping("/monthly-expense/scheduler")
    public ResponseEntity<ApiResponse<String>> manualTriggerForScheduler() throws Exception {
        log.info("Inside ExpenseController_manualTriggerForScheduler..");
        jobScheduler.monthlyExpenseSheetScheduler();
        log.info("Exiting ExpenseController_manualTriggerForScheduler..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully generated monthly expense file", null, null), HttpStatus.CREATED);
    }

}