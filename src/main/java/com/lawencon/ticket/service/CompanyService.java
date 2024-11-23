package com.lawencon.ticket.service;

import org.springframework.data.domain.Page;
import com.lawencon.ticket.model.request.PagingRequest;
import com.lawencon.ticket.model.request.company.CompanyRequestCreate;
import com.lawencon.ticket.model.request.company.CompanyRequestUpdate;
import com.lawencon.ticket.model.response.company.CompanyResponse;
import com.lawencon.ticket.persistence.entity.Company;

import java.util.List;

public interface CompanyService {
    CompanyResponse getById(String id);

    void create(CompanyRequestCreate request);

    void update(CompanyRequestUpdate request);

    Page<CompanyResponse> getAll(PagingRequest pagingRequest, String inquiry);

    void delete(String id);

    Company findEntityById(String companyId);

    List<CompanyResponse> getAllCompanies();
}
