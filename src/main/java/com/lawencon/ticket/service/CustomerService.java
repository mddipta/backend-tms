package com.lawencon.ticket.service;

import java.util.List;
import com.lawencon.ticket.model.request.PagingRequest;
import com.lawencon.ticket.model.request.customer.CreateCustomerRequest;
import com.lawencon.ticket.model.request.customer.UpdateCustomerRequest;
import com.lawencon.ticket.model.response.File;
import com.lawencon.ticket.model.response.customer.CustomerResponse;
import com.lawencon.ticket.persistence.entity.Customer;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Page<CustomerResponse> getAll(PagingRequest pagingRequest, String inquiry);

    void create(CreateCustomerRequest request);

    void update(UpdateCustomerRequest request);

    void delete(String id);

    CustomerResponse getByUserId(String id);

    CustomerResponse findById(String id);

    Customer findEntityById(String customerId);

    File getReport() throws JRException;

    List<CustomerResponse> getByPicId(String id);
}
