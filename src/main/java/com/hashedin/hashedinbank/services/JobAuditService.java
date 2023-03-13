package com.hashedin.hashedinbank.services;

import com.hashedin.hashedinbank.entities.JobAudit;

public interface JobAuditService {
    String STATUS_COMPLETED = "COMPLETED";
    String STATUS_STARTED = "STARTED";
    String STATUS_FAILED = "FAILED";
    JobAudit save(JobAudit jobAudit);
}
