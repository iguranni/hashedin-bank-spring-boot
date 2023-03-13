package com.hashedin.hashedinbank.services;

import com.hashedin.hashedinbank.dto.request.CompanyRegistrationDto;
import com.hashedin.hashedinbank.dto.request.CompanyUpdateDto;
import com.hashedin.hashedinbank.entities.Company;

import java.util.List;

public interface CompanyService {

    Company createCompany(CompanyRegistrationDto companyRegistrationDto);

    Company updateCompany(CompanyUpdateDto companyUpdateDto);

    void expireCompany(Integer companyId);

    List<Company> getAllCompanies();
}
