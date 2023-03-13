package com.hashedin.hashedinbank.utils;


import com.hashedin.hashedinbank.services.impl.JobAuditErrorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.hashedin.hashedinbank.constants.AppConstants.CREDIT_CARD_VALIDITY;
import static com.hashedin.hashedinbank.constants.AppConstants.MONTHLY_EXPENSE_CALCULATION;

@Slf4j
@Component
@EnableAsync
public class JobScheduler {

    private final ApplicationContext applicationContext;
    private final JobAuditErrorServiceImpl jobAuditErrorServiceImpl;

    public JobScheduler(ApplicationContext applicationContext,
                        JobAuditErrorServiceImpl jobAuditErrorServiceImpl) {
        this.applicationContext = applicationContext;
        this.jobAuditErrorServiceImpl = jobAuditErrorServiceImpl;
    }

    @Async
    @Scheduled(cron = "0 0 12 25 * ?")
    public void monthlyExpenseSheetScheduler() {
        JobAuditErrorServiceImpl jobAuditErrorService = applicationContext.getBean(JobAuditErrorServiceImpl.class);
        jobAuditErrorService.processJobAuditService(MONTHLY_EXPENSE_CALCULATION);
    }

    @Async
    @Scheduled(cron = "0 0 12 ? * SUN-SAT")
    public void checkCreditCardsValiditySheetScheduler(String email) {
        JobAuditErrorServiceImpl jobAuditErrorService = applicationContext.getBean(JobAuditErrorServiceImpl.class);
        jobAuditErrorService.processJobAuditService(CREDIT_CARD_VALIDITY);
    }

}