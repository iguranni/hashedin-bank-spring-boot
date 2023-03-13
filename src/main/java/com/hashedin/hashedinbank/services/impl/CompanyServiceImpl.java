package com.hashedin.hashedinbank.services.impl;

import com.hashedin.hashedinbank.dto.request.CompanyRegistrationDto;
import com.hashedin.hashedinbank.dto.request.CompanyUpdateDto;
import com.hashedin.hashedinbank.entities.Company;
import com.hashedin.hashedinbank.exception.CompanyAlreadyExistsException;
import com.hashedin.hashedinbank.exception.CompanyNotFoundException;
import com.hashedin.hashedinbank.repositories.CompanyRepository;
import com.hashedin.hashedinbank.services.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.hashedin.hashedinbank.utils.CommonUtils.isPastEffectiveEndDate;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }


    @Override
    @Transactional
    public Company createCompany(CompanyRegistrationDto companyRegistrationDto) {
        if (companyRepository.existsByCompanyCode(companyRegistrationDto.getCompanyCode())) {
            throw new CompanyAlreadyExistsException("Company already exists with company code : " + companyRegistrationDto.getCompanyCode());
        }

        return companyRepository.save(adaptCompanyRegistrationDto(companyRegistrationDto));
    }

    @Override
    @Transactional
    public Company updateCompany(CompanyUpdateDto companyUpdateDto) {
        Company existingCompany = companyRepository.findById(companyUpdateDto.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id : " + companyUpdateDto.getCompanyId()));

        return companyRepository.save(updateCompanyDetails(companyUpdateDto, existingCompany));
    }

    @Override
    @Transactional
    public void expireCompany(Integer companyId) {
        Company existingCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id : " + companyId));

        existingCompany.setEffectiveEndDate(LocalDateTime.now());
        existingCompany.setIsActiveFlag(false);
        companyRepository.save(existingCompany);
    }

    /**
     * @return
     */
    @Override
    @Transactional
    public List<Company> getAllCompanies() {
        return companyRepository.findAllByIsActiveFlag(Boolean.TRUE);
    }

    private Company adaptCompanyRegistrationDto(CompanyRegistrationDto companyRegistrationDto) {
        return Company.builder()
                .companyCode(companyRegistrationDto.getCompanyCode())
                .companyName(companyRegistrationDto.getCompanyName())
                .companyDescription(companyRegistrationDto.getCompanyDescription())
                .companyEmail(companyRegistrationDto.getCompanyEmail())
                .effectiveStartDate(companyRegistrationDto.getEffectiveStartDate())
                .build();
    }

    private Company updateCompanyDetails(CompanyUpdateDto companyUpdateDto, Company existingCompany) {
        existingCompany.setCompanyName(companyUpdateDto.getCompanyName());
        existingCompany.setCompanyDescription(companyUpdateDto.getCompanyDescription());
        existingCompany.setEffectiveEndDate(companyUpdateDto.getEffectiveEndDate());
        existingCompany.setIsActiveFlag(!isPastEffectiveEndDate(companyUpdateDto.getEffectiveEndDate()));

        return existingCompany;
    }
}
