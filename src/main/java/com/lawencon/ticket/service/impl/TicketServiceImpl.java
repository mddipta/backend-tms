package com.lawencon.ticket.service.impl;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import com.lawencon.ticket.model.response.File;
import com.lawencon.ticket.model.response.customer.CustomerResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.lawencon.ticket.model.request.ticket.ChangeStatusTicketRequest;
import com.lawencon.ticket.model.request.ticket.CreateTicketRequest;
import com.lawencon.ticket.model.request.ticket.UpdateTicketRequest;
import com.lawencon.ticket.model.request.transaction.CreateTicketTransactionRequest;
import com.lawencon.ticket.model.response.ticket.TicketResponse;
import com.lawencon.ticket.model.response.user.UserResponse;
import com.lawencon.ticket.persistence.entity.Customer;
import com.lawencon.ticket.persistence.entity.PriorityTicketStatus;
import com.lawencon.ticket.persistence.entity.Ticket;
import com.lawencon.ticket.persistence.entity.TicketTransaction;
import com.lawencon.ticket.persistence.repository.TicketRepository;
import com.lawencon.ticket.service.CustomerService;
import com.lawencon.ticket.service.PriorityTicketStatusService;
import com.lawencon.ticket.service.TicketService;
import com.lawencon.ticket.service.TicketTransactionService;
import com.lawencon.ticket.service.UserService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository repository;
    private final PriorityTicketStatusService priorityTicketStatusService;
    private final TicketTransactionService ticketTransactionService;
    private final CustomerService customerService;
    private final UserService userService;

    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int INVOICE_LENGTH = 5;

    private String generateTicketNumber() {
        StringBuilder invoiceCode = new StringBuilder(INVOICE_LENGTH);
        Random random = new Random();
        for (int i = 0; i < INVOICE_LENGTH; i++) {
            invoiceCode.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return invoiceCode.toString();
    }

    @Override
    public List<TicketResponse> getAll() {
        List<TicketResponse> responses = new ArrayList<>();
        List<Ticket> tickets = repository.findAll();
        for (Ticket ticket : tickets) {
            TicketTransaction ticketTransaction =
                    ticketTransactionService.getLastByTicketId(ticket.getId());
            TicketResponse response = mapToResponse(ticket, ticketTransaction);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public void create(CreateTicketRequest request) {
        LocalDate dateNow = LocalDate.now();
        String ticketCode = "#T-" + generateTicketNumber();

        Ticket ticket = new Ticket();

        ticket.setCode(ticketCode);
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setDueDate(request.getDueDate());
        ticket.setDateTicket(dateNow);

        PriorityTicketStatus priorityTicketStatus =
                priorityTicketStatusService.getEntityByCode(request.getPriorityTicketStatus());
        ticket.setPriorityTicketStatus(priorityTicketStatus);

        Customer customer = customerService.findEntityById(request.getCustomerId());
        ticket.setCustomer(customer);

        ticket.setUser(null);
        ticket.setIsActive(true);
        ticket.setVersion(0L);

        ticket = repository.save(ticket);

        // ticket transaction
        CreateTicketTransactionRequest ticketTransactionRequest =
                new CreateTicketTransactionRequest();
        Long lastTransactionNumber =
                ticketTransactionService.getLastByTicketId(ticket.getId()) != null
                        ? ticketTransactionService.getLastByTicketId(ticket.getId()).getNumber() + 1
                        : 1L;

        ticketTransactionRequest.setNumber(lastTransactionNumber);
        ticketTransactionRequest.setTicket(ticket);
        ticketTransactionRequest.setUser(null);
        ticketTransactionRequest.setStatus("OP");

        ticketTransactionService.create(ticketTransactionRequest);
    }


    @Override
    public void update(UpdateTicketRequest request) {
        validateExistId(request.getId());
        Ticket ticket = repository.findById(request.getId()).get();

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setDueDate(request.getDueDate());

        PriorityTicketStatus priorityTicketStatus =
                priorityTicketStatusService.getEntityByCode(request.getPriorityTicketStatus());
        ticket.setPriorityTicketStatus(priorityTicketStatus);

        ticket.setIsActive(request.getIsActive());
        ticket.setUpdatedAt(ZonedDateTime.now(ZoneOffset.UTC));
        ticket.setUpdatedBy("system");

        repository.saveAndFlush(ticket);
    }

    @Override
    public List<TicketResponse> getByUserId(String id) {
        List<TicketResponse> responses = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketTransaction ticketTransaction =
                    ticketTransactionService.getLastByTicketId(ticket.getId());
            TicketResponse response = mapToResponse(ticket, ticketTransaction);
            responses.add(response);
        }
        return responses;
    }


    @Override
    public TicketResponse getById(String id) {
        validateExistId(id);
        Ticket ticket = repository.findById(id).get();
        TicketTransaction ticketTransaction =
                ticketTransactionService.getLastByTicketId(ticket.getId());
        return mapToResponse(ticket, ticketTransaction);
    }

    @Override
    public Ticket getEntityById(String id) {
        validateExistId(id);
        return repository.findById(id).get();
    }

    @Override
    public void changeStatus(ChangeStatusTicketRequest request) {
        Ticket ticket = repository.findById(request.getId()).get();

        CreateTicketTransactionRequest ticketTransactionRequest =
                new CreateTicketTransactionRequest();

        ticketTransactionRequest.setUser(null);
        ticketTransactionRequest.setStatus(request.getStatus());

        TicketTransaction lastTransaction =
                ticketTransactionService.getLastByTicketId(ticket.getId());
        Long lastTransactionNumber = lastTransaction.getNumber() + 1;

        ticketTransactionRequest.setNumber(lastTransactionNumber);
        ticketTransactionRequest.setTicket(ticket);

        ticketTransactionService.create(ticketTransactionRequest);
    }

    @Override
    public File getReportTicket() throws JRException {
        File file = new File();
        file.setFileName("Daftar Ticket");
        file.setFileExt(".pdf");

        List<TicketResponse> reportData = getAll();

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("subTitle", "Daftar Tiket");
        parameters.put("data", dataSource.cloneDataSource());

        InputStream filePath =
                getClass().getClassLoader().getResourceAsStream("templates/ticket.jrxml");
        JasperReport report = JasperCompileManager.compileReport(filePath);

        JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

        byte[] bytes = JasperExportManager.exportReportToPdf(print);
        file.setData(bytes);

        return file;
    }

    private TicketResponse mapToResponse(Ticket ticket, TicketTransaction ticketTransaction) {
        TicketResponse response = new TicketResponse();
        response.setPriorityTicket(ticket.getPriorityTicketStatus().getCode());
        response.setStatus(ticketTransaction.getTicketStatus().getCode());
        response.setStatusName(ticketTransaction.getTicketStatus().getName());
        response.setPic(ticket.getCustomer().getPicUser().getName());
        response.setDate(ticket.getDateTicket());

        if (ticket.getUser() == null || ticket.getUser().getName() == null) {
            response.setDeveloper("-");
        } else {
            response.setDeveloper(ticket.getUser().getName());
        }


        BeanUtils.copyProperties(ticket, response);
        return response;
    }

    private void validateExistId(String id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<TicketResponse> getByCustomerId(String id) {
        List<TicketResponse> responses = new ArrayList<>();
        List<Ticket> tickets = repository.findByCustomerId(id);
        for (Ticket ticket : tickets) {
            TicketTransaction ticketTransaction =
                    ticketTransactionService.getLastByTicketId(ticket.getId());
            TicketResponse response = mapToResponse(ticket, ticketTransaction);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public List<TicketResponse> getByUser(String id) {
        // Check role user
        UserResponse user = userService.getById(id);
        if (user.getRole().equals("CUS")) {
            CustomerResponse customerResponse = customerService.getByUserId(id);
            String customerId = customerResponse.getId();

            List<Ticket> tickets = repository.findByCustomerId(customerId);
            List<TicketResponse> responses = new ArrayList<>();
            for (Ticket ticket : tickets) {
                TicketTransaction ticketTransaction =
                        ticketTransactionService.getLastByTicketId(ticket.getId());
                TicketResponse response = mapToResponse(ticket, ticketTransaction);
                responses.add(response);
            }

            return responses;
        } else if (user.getRole().equals("DEV")) {
            List<Ticket> tickets = repository.findByUserId(id);
            List<TicketResponse> responses = new ArrayList<>();
            for (Ticket ticket : tickets) {
                TicketTransaction ticketTransaction =
                        ticketTransactionService.getLastByTicketId(ticket.getId());
                TicketResponse response = mapToResponse(ticket, ticketTransaction);
                responses.add(response);
            }

            return responses;
        } else if (user.getRole().equals("PIC")) {
            List<CustomerResponse> customerResponses = customerService.getByPicId(id);
            List<TicketResponse> responses = new ArrayList<>();
            for (CustomerResponse customerResponse : customerResponses) {
                List<Ticket> tickets = repository.findByCustomerId(customerResponse.getId());
                for (Ticket ticket : tickets) {
                    TicketTransaction ticketTransaction =
                            ticketTransactionService.getLastByTicketId(ticket.getId());
                    TicketResponse response = mapToResponse(ticket, ticketTransaction);
                    responses.add(response);
                }
            }

            return responses;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


}
