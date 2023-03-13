package com.hashedin.hashedinbank.controllers;

import com.hashedin.hashedinbank.common.ApiResponse;
import com.hashedin.hashedinbank.dto.request.CompanyRegistrationDto;
import com.hashedin.hashedinbank.dto.request.CompanyUpdateDto;
import com.hashedin.hashedinbank.entities.Company;
import com.hashedin.hashedinbank.services.CompanyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Company>> registerCompany(@Valid @RequestBody CompanyRegistrationDto companyRegistrationDto) {
        log.info("Inside CompanyController_registerCompany..");
        Company company = companyService.createCompany(companyRegistrationDto);
        log.info("Exiting CompanyController_registerCompany..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully registered company with HashedIn Bank for company code : " + companyRegistrationDto.getCompanyCode(),
                null, company), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Company>> updateCompany(@Valid @RequestBody CompanyUpdateDto companyUpdateDto) {
        log.info("Inside CompanyController_updateCompany..");
        Company company = companyService.updateCompany(companyUpdateDto);
        log.info("Exiting CompanyController_updateCompany..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully updated company details with HashedIn Bank for company id : " + companyUpdateDto.getCompanyId(),
                null, company), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteCompany(@RequestParam Integer companyId) {
        log.info("Inside CompanyController_deleteCompany..");
        companyService.expireCompany(companyId);
        log.info("Exiting CompanyController_deleteCompany..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully deleted company from HashedIn Bank for company id : " + companyId,
                null, null), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<Company>>> findAllCompanies() {
        log.info("Inside CompanyController_findAllCompanies..");
        List<Company> companies = companyService.getAllCompanies();
        log.info("Exiting CompanyController_findAllCompanies..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully fetched all clients of HashedIn Bank : ",
                companies.size(), companies), HttpStatus.OK);
    }
}