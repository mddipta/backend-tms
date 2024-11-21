package com.lawencon.ticket.controller;

import java.util.List;

import com.lawencon.ticket.model.response.File;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.ticket.helper.ResponseHelper;
import com.lawencon.ticket.model.request.customer.CreateCustomerRequest;
import com.lawencon.ticket.model.response.WebResponse;
import com.lawencon.ticket.model.response.customer.CustomerResponse;
import com.lawencon.ticket.service.CustomerService;

import org.springframework.web.bind.annotation.RequestBody;

import com.lawencon.ticket.model.request.customer.UpdateCustomerRequest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Customer", description = "Customer API endpoint")
@RestController
@RequestMapping({"/api/v1"})
@AllArgsConstructor
public class CustomerController {
    private CustomerService customerService;

    @GetMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<CustomerResponse>>> findAll() {
        return ResponseEntity.ok(ResponseHelper.ok(customerService.getAll()));
    }

    @GetMapping(value = "/customers/pic/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<CustomerResponse>>> findByPicId(
            @PathVariable String id) {
        return ResponseEntity.ok(ResponseHelper.ok(customerService.getByPicId(id)));
    }

    @GetMapping(value = "/customer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<CustomerResponse>> findById(@PathVariable String id) {
        return ResponseEntity.ok(ResponseHelper.ok(customerService.findById(id)));
    }

    @GetMapping(value = "/customers/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<CustomerResponse>> findByUserId(@PathVariable String id) {
        return ResponseEntity.ok(ResponseHelper.ok(customerService.getByUserId(id)));
    }

    @PostMapping(value = "/customer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> add(
            @Valid @RequestBody CreateCustomerRequest request) {
        customerService.create(request);
        return ResponseEntity.ok(ResponseHelper.ok("Create customer success"));
    }

    @PutMapping(value = "/customer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> update(
            @Valid @RequestBody UpdateCustomerRequest request) {
        customerService.update(request);
        return ResponseEntity.ok(ResponseHelper.ok("Update customer success"));
    }

    @DeleteMapping(value = "/customer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> delete(@PathVariable String id) {
        customerService.delete(id);
        return ResponseEntity.ok(ResponseHelper.ok("Delete customer success"));
    }

    @GetMapping(value = "/customer/report", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<File>> generateReport() throws JRException {
        return ResponseEntity.ok(ResponseHelper.ok(customerService.getReport()));
    }
}
