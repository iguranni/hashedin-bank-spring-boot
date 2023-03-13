package com.hashedin.hashedinbank.services.impl;

import com.hashedin.hashedinbank.entities.JobErrorInfo;
import com.hashedin.hashedinbank.repositories.JobErrorInfoRepository;
import com.hashedin.hashedinbank.services.JobErrorInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobErrorInfoServiceImpl implements JobErrorInfoService {

    private final JobErrorInfoRepository jobErrorInfoRepository;

    @Autowired
    public JobErrorInfoServiceImpl(JobErrorInfoRepository jobErrorInfoRepository) {
        this.jobErrorInfoRepository = jobErrorInfoRepository;
    }

    /**
     * @param jobErrorInfo
     */
    @Override
    public void save(JobErrorInfo jobErrorInfo) {
        try {
            jobErrorInfoRepository.save(jobErrorInfo);
        } catch (Exception e) {
            log.info("Error inserting record in job audit");
        }
    }
}
