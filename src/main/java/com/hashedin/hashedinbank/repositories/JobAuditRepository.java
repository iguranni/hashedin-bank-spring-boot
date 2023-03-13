package com.hashedin.hashedinbank.repositories;

import com.hashedin.hashedinbank.entities.JobAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAuditRepository extends JpaRepository<JobAudit, Integer> {
}
