package com.lawencon.ticket.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.lawencon.ticket.persistence.entity.Company;

public interface CompanyRepository
        extends JpaRepository<Company, String>, JpaSpecificationExecutor<Company> {

}
