package com.hashedin.hashedinbank.repositories;

import com.hashedin.hashedinbank.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Optional<Company> findByCompanyCode(String companyCode);

    Boolean existsByCompanyCode(String companyCode);

    Optional<Company> findByCompanyIdAndIsActiveFlag(Integer id, Boolean isActiveFlag);

    List<Company> findAllByIsActiveFlag(Boolean isActiveFlag);
}
