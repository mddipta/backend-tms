package com.lawencon.ticket.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.ticket.helper.ResponseHelper;
import com.lawencon.ticket.model.request.PagingRequest;
import com.lawencon.ticket.model.request.SortBy;
import com.lawencon.ticket.model.request.company.CompanyRequestCreate;
import com.lawencon.ticket.model.request.company.CompanyRequestUpdate;
import com.lawencon.ticket.model.response.WebResponse;
import com.lawencon.ticket.model.response.company.CompanyResponse;
import com.lawencon.ticket.service.CompanyService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Company", description = "Company API endpoint")
@RestController
@RequestMapping({"/api/v1"})
@AllArgsConstructor
public class CompanyController {
    final private CompanyService service;

    @GetMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<CompanyResponse>>> findAll(PagingRequest pagingRequest,
            @RequestParam(required = false) String inquiry) {
        return ResponseEntity.ok(ResponseHelper.ok(pagingRequest, service.getAll(pagingRequest, inquiry)));
    }

    @GetMapping(value = "/company/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<CompanyResponse>> findById(@PathVariable String id) {
        return ResponseEntity.ok(ResponseHelper.ok(service.getById(id)));
    }

    @PostMapping(value = "/company", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> add(@Valid @RequestBody CompanyRequestCreate request) {
        service.create(request);
        return ResponseEntity.ok("Success");
    }

    @PutMapping(value = "/company", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update(@Valid @RequestBody CompanyRequestUpdate request) {
        service.update(request);
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping(value = "/company/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("Success");
    }
}
