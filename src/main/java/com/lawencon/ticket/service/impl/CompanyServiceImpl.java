package com.lawencon.ticket.service.impl;


import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.lawencon.ticket.helper.SpecificationHelper;
import com.lawencon.ticket.model.request.PagingRequest;
import com.lawencon.ticket.model.request.company.CompanyRequestCreate;
import com.lawencon.ticket.model.request.company.CompanyRequestUpdate;
import com.lawencon.ticket.model.response.company.CompanyResponse;
import com.lawencon.ticket.persistence.entity.Company;
import com.lawencon.ticket.persistence.repository.CompanyRepository;
import com.lawencon.ticket.service.CompanyService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    final private CompanyRepository repository;

    @Override
    public CompanyResponse getById(String id) {
        Optional<Company> company = repository.findById(id);
        if (company.isPresent()) {
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Id not found");
        }
        return mapToResponse(company.get());
    }

    @Override
    public void create(CompanyRequestCreate request) {
        Company company = new Company();
        company.setName(request.getName());
        company.setAddress(request.getAddress());
        company.setIsActive(true);

        repository.save(company);
    }

    @Override
    public void update(CompanyRequestUpdate request) {
        validateIdExist(request.getId());

        Company company = repository.findById(request.getId()).get();
        company.setName(request.getName());
        company.setAddress(request.getAddress());
        company.setIsActive(request.getIsActive());
        company.setVersion(request.getVersion() + 1);

        repository.saveAndFlush(company);
    }

    @Override
    public Page<CompanyResponse> getAll(PagingRequest pagingRequest, String inquiry) {
        PageRequest pageRequest =
                PageRequest.of(pagingRequest.getPage(), pagingRequest.getPageSize(),
                        SpecificationHelper.createSort(pagingRequest.getSortBy()));

        Specification<Company> spec = Specification.where(null);
        if (inquiry != null) {
            spec = spec.and(SpecificationHelper.inquiryFilter(Arrays.asList("name"), inquiry));
        }

        Page<Company> companyResponses = repository.findAll(spec, pageRequest);

        List<CompanyResponse> responses = companyResponses.getContent().stream().map(company -> {
            CompanyResponse companyResponse = new CompanyResponse();
            BeanUtils.copyProperties(company, companyResponse);
            return companyResponse;
        }).toList();
        return new PageImpl<>(responses, pageRequest, companyResponses.getTotalElements());
    }

    @Override
    public void delete(String id) {
        validateIdExist(id);
        Company company = repository.findById(id).get();
        company.setDeletedAt(ZonedDateTime.now(ZoneOffset.UTC));
        repository.delete(company);
    }

    @Override
    public Company findEntityById(String companyId) {
        Optional<Company> company = repository.findById(companyId);
        if (company.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Id not found");
        }
        return company.get();
    }

    private CompanyResponse mapToResponse(Company company) {
        CompanyResponse response = new CompanyResponse();
        BeanUtils.copyProperties(company, response);
        return response;
    }

    private void validateIdExist(String id) {
        boolean isExist = repository.existsById(id);
        if (!isExist) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Id not found");
        }
    }


}
