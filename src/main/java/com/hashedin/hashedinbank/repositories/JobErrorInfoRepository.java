package com.hashedin.hashedinbank.repositories;

import com.hashedin.hashedinbank.entities.JobErrorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobErrorInfoRepository extends JpaRepository<JobErrorInfo, Integer> {
}