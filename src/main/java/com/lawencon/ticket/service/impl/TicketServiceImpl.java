package com.lawencon.ticket.service.impl;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.lawencon.ticket.model.response.File;
import com.lawencon.ticket.model.response.customer.CustomerResponse;
import com.lawencon.ticket.persistence.entity.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lawencon.ticket.authentication.helper.SessionHelper;
import com.lawencon.ticket.helper.SpecificationHelper;
import com.lawencon.ticket.model.request.PagingRequest;
import com.lawencon.ticket.model.request.ticket.AssignDeveloperTicketRequest;
import com.lawencon.ticket.model.request.ticket.ChangeStatusTicketRequest;
import com.lawencon.ticket.model.request.ticket.CreateTicketRequest;
import com.lawencon.ticket.model.request.ticket.ProcessTicketRequest;
import com.lawencon.ticket.model.request.ticket.UpdateTicketRequest;
import com.lawencon.ticket.model.request.transaction.CreateTicketTransactionRequest;
import com.lawencon.ticket.model.response.ticket.TicketResponse;
import com.lawencon.ticket.model.response.user.UserResponse;
import com.lawencon.ticket.persistence.repository.TicketRepository;
import com.lawencon.ticket.service.CustomerService;
import com.lawencon.ticket.service.PriorityTicketStatusService;
import com.lawencon.ticket.service.TicketService;
import com.lawencon.ticket.service.TicketStatusService;
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
    private final TicketStatusService ticketStatusService;

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
    public Page<TicketResponse> getAll(PagingRequest pagingRequest, String inquiry) {
        PageRequest pageRequest =
                PageRequest.of(pagingRequest.getPage(), pagingRequest.getPageSize(),
                        SpecificationHelper.createSort(pagingRequest.getSortBy()));

        Specification<Ticket> spec = Specification.where(null);
        if (inquiry != null) {
            spec = spec.and(
                    SpecificationHelper.inquiryFilter(Arrays.asList("title", "code"), inquiry));
        }

        Page<Ticket> ticketResponses = repository.findAll(spec, pageRequest);

        List<TicketResponse> responses = ticketResponses.getContent().stream().map(ticket -> {
            TicketTransaction ticketTransaction =
                    ticketTransactionService.getLastByTicketId(ticket.getId());
            TicketResponse ticketResponse = mapToResponse(ticket, ticketTransaction);
            return ticketResponse;
        }).toList();

        return new PageImpl<>(responses, pageRequest, ticketResponses.getTotalElements());
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
                priorityTicketStatusService.getEntityById(request.getPriorityTicketStatus());
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
                priorityTicketStatusService.getEntityById(request.getPriorityTicketStatus());
        ticket.setPriorityTicketStatus(priorityTicketStatus);

        TicketStatus ticketStatus = ticketStatusService.getEntityById(request.getStatus());
        String codeStatus = ticketStatus.getCode();

        CreateTicketTransactionRequest ticketTransactionRequest =
                new CreateTicketTransactionRequest();

        ticketTransactionRequest.setUser(null);
        ticketTransactionRequest.setStatus(codeStatus);

        TicketTransaction lastTransaction =
                ticketTransactionService.getLastByTicketId(ticket.getId());
        Long lastTransactionNumber = lastTransaction.getNumber() + 1;

        ticketTransactionRequest.setNumber(lastTransactionNumber);
        ticketTransactionRequest.setTicket(ticket);

        ticketTransactionService.create(ticketTransactionRequest);

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

    // @Override
    // public File getReportTicket() throws JRException {
    // File file = new File();
    // file.setFileName("Daftar Ticket");
    // file.setFileExt(".pdf");

    // List<TicketResponse> reportData = getAll();

    // JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

    // Map<String, Object> parameters = new HashMap<>();
    // parameters.put("subTitle", "Daftar Tiket");
    // parameters.put("data", dataSource.cloneDataSource());

    // InputStream filePath =
    // getClass().getClassLoader().getResourceAsStream("templates/ticket.jrxml");
    // JasperReport report = JasperCompileManager.compileReport(filePath);

    // JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

    // byte[] bytes = JasperExportManager.exportReportToPdf(print);
    // file.setData(bytes);

    // return file;
    // }

    private TicketResponse mapToResponse(Ticket ticket, TicketTransaction ticketTransaction) {
        TicketResponse response = new TicketResponse();
        response.setPriorityTicket(ticket.getPriorityTicketStatus().getCode());
        response.setStatusId(ticketTransaction.getTicketStatus().getId());
        response.setStatusName(ticketTransaction.getTicketStatus().getName());
        response.setPic(ticket.getCustomer().getPicUser().getName());
        response.setDate(ticket.getDateTicket());
        response.setPriorityTicketId(ticket.getPriorityTicketStatus().getId());

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

    // @Override
    // public List<TicketResponse> getByCustomerId(String id) {
    // List<TicketResponse> responses = new ArrayList<>();
    // List<Ticket> tickets = repository.findByCustomerId(id);
    // for (Ticket ticket : tickets) {
    // TicketTransaction ticketTransaction =
    // ticketTransactionService.getLastByTicketId(ticket.getId());
    // TicketResponse response = mapToResponse(ticket, ticketTransaction);
    // responses.add(response);
    // }
    // return responses;
    // }

    @Override
    public Page<TicketResponse> getByUser(PagingRequest pagingRequest, String inquiry) {
        // Mendapatkan user login
        User userLogin = SessionHelper.getLoginUser();
        String userId = userLogin.getId();


        // Membuat PageRequest
        PageRequest pageRequest =
                PageRequest.of(pagingRequest.getPage(), pagingRequest.getPageSize(),
                        SpecificationHelper.createSort(pagingRequest.getSortBy()));

        // Membuat Specification
        Specification<Ticket> spec = Specification.where(null);

        // Menambahkan filter inquiry
        if (inquiry != null) {
            spec = spec.and(
                    SpecificationHelper.inquiryFilter(Arrays.asList("title", "code"), inquiry));
        }

        // Menambahkan filter berdasarkan role
        if (userLogin.getRole().getCode().equals("DEV")) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .equal(root.get("user").get("id"), userId));
        } else if (userLogin.getRole().getCode().equals("PIC")) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .equal(root.get("customer").get("picUser").get("id"), userId));
        } else if (userLogin.getRole().getCode().equals("CUS")) {
            String customerId = customerService.getByUserId(userLogin.getId()).getId();
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .equal(root.get("customer").get("id"), customerId));
        }

        // Query data dengan Specification
        Page<Ticket> ticketResponses = repository.findAll(spec, pageRequest);

        // Mapping ke response
        List<TicketResponse> responses = ticketResponses.getContent().stream().map(ticket -> {
            TicketTransaction ticketTransaction =
                    ticketTransactionService.getLastByTicketId(ticket.getId());
            return mapToResponse(ticket, ticketTransaction);
        }).collect(Collectors.toList());

        return new PageImpl<>(responses, pageRequest, ticketResponses.getTotalElements());
    }

    @Override
    public void assignTicket(AssignDeveloperTicketRequest request) {
        Ticket ticket = repository.findById(request.getTicketId()).get();
        User user = userService.findEntityById(request.getUserId());

        CreateTicketTransactionRequest ticketTransactionRequest =
                new CreateTicketTransactionRequest();

        ticketTransactionRequest.setUser(null);
        ticketTransactionRequest.setStatus("PA");

        TicketTransaction lastTransaction =
                ticketTransactionService.getLastByTicketId(ticket.getId());
        Long lastTransactionNumber = lastTransaction.getNumber() + 1;

        ticketTransactionRequest.setNumber(lastTransactionNumber);
        ticketTransactionRequest.setTicket(ticket);

        ticketTransactionService.create(ticketTransactionRequest);

        ticket.setUser(user);
        repository.saveAndFlush(ticket);
    }

    @Override
    public void processTicket(ProcessTicketRequest request) {
        User userLogin = SessionHelper.getLoginUser();

        if (!userLogin.getRole().getCode().equals("DEV")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Ticket ticket = repository.findById(request.getTicketId()).get();

        CreateTicketTransactionRequest ticketTransactionRequest =
                new CreateTicketTransactionRequest();

        ticketTransactionRequest.setUser(userLogin);
        ticketTransactionRequest.setStatus("OG");

        TicketTransaction lastTransaction =
                ticketTransactionService.getLastByTicketId(ticket.getId());
        Long lastTransactionNumber = lastTransaction.getNumber() + 1;

        ticketTransactionRequest.setNumber(lastTransactionNumber);
        ticketTransactionRequest.setTicket(ticket);

        ticketTransactionService.create(ticketTransactionRequest);
    }
}
