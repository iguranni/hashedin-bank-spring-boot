package com.hashedin.hashedinbank.services.impl;

import com.hashedin.hashedinbank.entities.JobAudit;
import com.hashedin.hashedinbank.entities.JobErrorInfo;
import com.hashedin.hashedinbank.services.JobAuditErrorService;
import com.hashedin.hashedinbank.services.JobAuditService;
import com.hashedin.hashedinbank.services.JobErrorInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.hashedin.hashedinbank.constants.AppConstants.CREDIT_CARD_VALIDITY;
import static com.hashedin.hashedinbank.constants.AppConstants.MONTHLY_EXPENSE_CALCULATION;
import static com.hashedin.hashedinbank.services.JobAuditService.STATUS_COMPLETED;
import static com.hashedin.hashedinbank.services.JobAuditService.STATUS_FAILED;

@Slf4j
@Component
@Scope("prototype")
public class JobAuditErrorServiceImpl implements JobAuditErrorService {
    private final ExpenseServiceImpl exportToExcelService;
    private final JobAuditService jobAuditService;
    private final JobErrorInfoService jobErrorInfoService;
    private JobAudit jobAudit;
    private Integer jobId;

    @Autowired
    public JobAuditErrorServiceImpl(ExpenseServiceImpl exportToExcelService,
                                    JobAuditService jobAuditService,
                                    JobErrorInfoService jobErrorInfoService) {
        this.exportToExcelService = exportToExcelService;
        this.jobAuditService = jobAuditService;
        this.jobErrorInfoService = jobErrorInfoService;
    }

    private void initialize(String jobType) {
        jobAudit = JobAudit.builder()
                .jobName(jobType)
                .jobStatus(JobAuditService.STATUS_STARTED)
                .startTime(LocalDateTime.now())
                .comments("Started Process")
                .build();
        this.jobAudit = jobAuditService.save(jobAudit);
        this.jobId = jobAudit.getJobId();
    }

    public void processJobAuditService(String jobType) {
        try {
            initialize(jobType);
            switch (jobType) {
                case MONTHLY_EXPENSE_CALCULATION -> {
                    exportToExcelService.calculateMonthlyExpenseByCompany();
                    parseSuccess(STATUS_COMPLETED, "Successfully generated monthly expense sheet");

                }
                case CREDIT_CARD_VALIDITY ->
                    //logic
                        parseSuccess(STATUS_COMPLETED, "Successfully validated credit cards");

                default -> throw new IllegalArgumentException("Invalid Job Type");
            }
        } catch (Exception e) {
            parseFailed("Exception while processing expense file", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * @param comment
     */
    @Override
    public void parseSuccess(String status, String comment) {
        prepareJobAudit(status, comment);
    }

    private JobAudit prepareJobAudit(String status, String comment) {
        jobAudit.setEndTime(LocalDateTime.now());
        jobAudit.setJobStatus(status);
        jobAudit.setComments(comment);
        return jobAudit;
    }

    /**
     * @param errorDescription
     * @param errorStackTrace
     */
    @Override
    public void parseFailed(String errorDescription, String errorStackTrace) {
        jobAuditService.save(prepareJobAudit(STATUS_FAILED, "Error Occurred during process"));
        jobErrorInfoService.save(prepareJobErrorInfo(errorDescription, errorStackTrace));
    }


    /**
     * @param errorDescription
     * @param errorStackTrace
     */
    @Override
    public void saveJobErrorInfo(String errorDescription, String errorStackTrace) {
        jobErrorInfoService.save(prepareJobErrorInfo(errorDescription, errorStackTrace));

    }

    private JobErrorInfo prepareJobErrorInfo(String errorDescription, String errorStackTrace) {
        return JobErrorInfo.builder()
                .jobId(jobId)
                .errorDescription(errorDescription)
                .errorStackTrace(errorStackTrace)
                .errorTimestamp(LocalDateTime.now())
                .build();
    }
}
