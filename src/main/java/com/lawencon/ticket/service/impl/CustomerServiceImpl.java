package com.lawencon.ticket.service.impl;

import java.io.InputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import com.lawencon.ticket.model.response.File;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lawencon.ticket.model.request.customer.CreateCustomerRequest;
import com.lawencon.ticket.model.request.customer.UpdateCustomerRequest;
import com.lawencon.ticket.model.response.customer.CustomerResponse;
import com.lawencon.ticket.persistence.entity.Company;
import com.lawencon.ticket.persistence.entity.Customer;
import com.lawencon.ticket.persistence.entity.User;
import com.lawencon.ticket.persistence.repository.CustomerRepository;
import com.lawencon.ticket.service.CompanyService;
import com.lawencon.ticket.service.CustomerService;
import com.lawencon.ticket.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository repository;
    UserService userService;
    CompanyService companyService;


    @Override
    public List<CustomerResponse> getAll() {
        List<CustomerResponse> responses = new ArrayList<>();
        List<Customer> customers = repository.findAll();
        for (Customer customer : customers) {
            CustomerResponse response = mapToRespoonse(customer);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public void create(CreateCustomerRequest request) {
        validateCompanyIdExist(request.getCompanyId());
        validateUserIdExist(request.getUserId());
        validateUserIdExist(request.getPicId());

        Customer customer = new Customer();

        User user = userService.findEntityById(request.getUserId());
        user.setId(user.getId());
        customer.setUser(user);

        Company company = companyService.findEntityById(request.getCompanyId());
        company.setId(company.getId());
        customer.setCompany(company);

        User picUser = userService.findEntityById(request.getPicId());
        picUser.setId(picUser.getId());
        customer.setPicUser(picUser);

        customer.setIsActive(true);

        repository.save(customer);
    }

    @Override
    public void update(UpdateCustomerRequest request) {
        Customer customer = repository.findById(request.getId()).get();
        if (!request.getUserId().equals(customer.getUser().getId())) {
            validateUserIdExist(request.getUserId());
        }

        if (!request.getCompanyId().equals(customer.getCompany().getId())) {
            validateCompanyIdExist(request.getCompanyId());
        }

        if (!request.getPicId().equals(customer.getPicUser().getId())) {
            validateUserIdExist(request.getPicId());
        }

        User user = userService.findEntityById(request.getUserId());
        user.setId(user.getId());
        customer.setUser(user);

        User picUser = userService.findEntityById(request.getPicId());
        picUser.setId(picUser.getId());
        customer.setPicUser(picUser);

        Company company = companyService.findEntityById(request.getCompanyId());
        company.setId(company.getId());
        customer.setCompany(company);

        customer.setIsActive(request.getIsActive());
        customer.setVersion(request.getVersion() + 1);

        repository.saveAndFlush(customer);
    }


    @Override
    public void delete(String id) {
        validateUserIdExist(id);
        Customer customer = repository.findById(id).get();
        customer.setDeletedAt(ZonedDateTime.now(ZoneOffset.UTC));
        repository.delete(customer);
    }

    @Override
    public CustomerResponse findById(String id) {
        validateUserIdExist(id);
        Optional<Customer> customer = repository.findById(id);
        return mapToRespoonse(customer.get());
    }

    @Override
    public Customer findEntityById(String customerId) {
        Optional<Customer> customer = repository.findById(customerId);
        if (customer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Id not found");
        }
        return customer.get();
    }

    @Override
    public File getReport() throws JRException {
        File file = new File();
        file.setFileName("Daftar Customer");
        file.setFileExt(".pdf");

        List<CustomerResponse> reportData = repository.findAll().stream().map(this::mapToRespoonse).toList();

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("customer", "Daftar Customer Perusahaan A");
        parameters.put("tableData", dataSource.cloneDataSource());

        InputStream filePath = getClass().getClassLoader().getResourceAsStream("templates/customer.jrxml");
        JasperReport report = JasperCompileManager.compileReport(filePath);

        JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

        byte[] bytes = JasperExportManager.exportReportToPdf(print);
        file.setData(bytes);

        return file;
    }

    private CustomerResponse mapToRespoonse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setCompany(customer.getCompany().getName());
        response.setCompanyId(customer.getCompany().getId());
        response.setUserId(customer.getUser().getId());
        response.setUser(customer.getUser().getUsername());
        response.setName(customer.getUser().getName());
        response.setPicId(customer.getPicUser().getId());
        response.setPicName(customer.getPicUser().getName());
        BeanUtils.copyProperties(customer, response);
        return response;
    }

    private void validateUserIdExist(String id) {
        Optional<Customer> customer = repository.findByUserId(id);
        if (customer.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Id not found");
        }
    }

    private void validateCompanyIdExist(String id) {
        Optional<Customer> customer = repository.findByCompanyId(id);
        if (customer.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Id not found");
        }
    }
}
