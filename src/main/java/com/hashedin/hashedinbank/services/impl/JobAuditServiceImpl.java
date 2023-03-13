package com.hashedin.hashedinbank.services.impl;

import com.hashedin.hashedinbank.entities.JobAudit;
import com.hashedin.hashedinbank.repositories.JobAuditRepository;
import com.hashedin.hashedinbank.services.JobAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobAuditServiceImpl implements JobAuditService {

    private final JobAuditRepository jobAuditRepository;

    @Autowired
    public JobAuditServiceImpl(JobAuditRepository jobAuditRepository) {
        this.jobAuditRepository = jobAuditRepository;
    }


    /**
     * @param jobAudit
     * @return
     */
    @Override
    public JobAudit save(JobAudit jobAudit) {
        try {
            jobAudit = jobAuditRepository.save(jobAudit);
        } catch (Exception e) {
            log.info("Error inserting record in job audit");
        }
        return jobAudit;
    }
}
