package com.hashedin.hashedinbank.services.impl;

import com.hashedin.hashedinbank.HashedinBankApplicationTests;
import com.hashedin.hashedinbank.entities.Company;
import com.hashedin.hashedinbank.exception.CompanyAlreadyExistsException;
import com.hashedin.hashedinbank.exception.CompanyNotFoundException;
import com.hashedin.hashedinbank.repositories.CompanyRepository;
import com.hashedin.hashedinbank.utils.ConstructTestObjectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

class CompanyServiceImplTest extends HashedinBankApplicationTests {
    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyServiceImpl companyServiceImpl;


    @Test
    void createCompanyTest() {
        Company company = ConstructTestObjectUtil.constructCompany();

        Mockito.when(companyRepository.existsByCompanyCode(anyString())).thenReturn(false);
        Mockito.when(companyRepository.save(any(Company.class))).thenReturn(company);

        Company actualCompany = companyServiceImpl.createCompany(ConstructTestObjectUtil.constructCompanyRegistrationDto());

        Assertions.assertNotNull(actualCompany);
    }

    @Test
    void createCompanyTest_whenThrowsException() {
        Mockito.when(companyRepository.existsByCompanyCode(anyString())).thenReturn(true);

        CompanyAlreadyExistsException exception = Assertions.assertThrows(CompanyAlreadyExistsException.class,
                () -> companyServiceImpl.createCompany(ConstructTestObjectUtil.constructCompanyRegistrationDto()));

        Assertions.assertEquals("Company already exists with company code : " + ConstructTestObjectUtil.constructCompanyRegistrationDto().getCompanyCode(), exception.getMessage());
    }

    @Test
    void updateCompanyTest() {
        Company company = ConstructTestObjectUtil.constructCompany();

        Mockito.when(companyRepository.findById(anyInt())).thenReturn(Optional.ofNullable(company));
        Mockito.when(companyRepository.save(any(Company.class))).thenReturn(company);

        Company updatedCompany = companyServiceImpl.updateCompany(ConstructTestObjectUtil.constructCompanyUpdateDto());

        Assertions.assertNotNull(updatedCompany);
    }

    @Test
    void updateCompanyTest_whenEffectiveEndDateIsPast() {
        Company company = ConstructTestObjectUtil.constructCompany();

        Mockito.when(companyRepository.findById(anyInt())).thenReturn(Optional.ofNullable(company));
        Mockito.when(companyRepository.save(any(Company.class))).thenReturn(company);

        Company updatedCompany = companyServiceImpl.updateCompany(ConstructTestObjectUtil.constructCompanyUpdateDto());

        Assertions.assertNotNull(updatedCompany);
        Assertions.assertEquals(updatedCompany.getIsActiveFlag(), false);
    }

    @Test
    void updateCompanyTest_whenThrowsException() {
        Mockito.when(companyRepository.findById(anyInt())).thenThrow(new CompanyNotFoundException("Testing exception block.."));
    }

    @Test
    void expireCompanyTest() {
        Company company = ConstructTestObjectUtil.constructCompany();

        Mockito.when(companyRepository.findById(anyInt())).thenReturn(Optional.ofNullable(company));

        companyServiceImpl.expireCompany(ConstructTestObjectUtil.constructCompanyUpdateDto().getCompanyId());
    }

    @Test
    void getAllCompaniesTest() {
        Company company = ConstructTestObjectUtil.constructCompany();

        Mockito.when(companyRepository.findAllByIsActiveFlag(anyBoolean())).thenReturn(List.of(company));

        List<Company> allCompanies = companyServiceImpl.getAllCompanies();

        Assertions.assertNotNull(allCompanies);

    }
}