package com.lawencon.ticket.controller;

import java.util.List;

import com.lawencon.ticket.model.response.File;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.lawencon.ticket.model.request.ticket.ChangeStatusTicketRequest;
import com.lawencon.ticket.model.request.ticket.CreateTicketRequest;
import com.lawencon.ticket.model.request.ticket.UpdateTicketRequest;
import com.lawencon.ticket.model.response.WebResponse;
import com.lawencon.ticket.model.response.ticket.TicketResponse;
import com.lawencon.ticket.service.TicketService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Ticket", description = "Ticket API endpoint")
@RestController
@RequestMapping({"/api/v1"})
@AllArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping(value = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<TicketResponse>>> findAll(PagingRequest pagingRequest,
            @RequestParam(required = false) String inquiry) {
        return ResponseEntity
                .ok(ResponseHelper.ok(pagingRequest, ticketService.getAll(pagingRequest, inquiry)));
    }

    // @GetMapping(value = "/tickets/customer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<WebResponse<List<TicketResponse>>> findByCustomerId(
    // @PathVariable String id) {
    // return ResponseEntity.ok(ResponseHelper.ok(ticketService.getByCustomerId(id)));
    // }

    @GetMapping(value = "/tickets/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<TicketResponse>>> findByUser(PagingRequest pagingRequest,
            @RequestParam(required = false) String inquiry) {
        return ResponseEntity.ok(
                ResponseHelper.ok(pagingRequest, ticketService.getByUser(pagingRequest, inquiry)));
    }

    @PostMapping(value = "/ticket", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> add(
            @Valid @RequestBody CreateTicketRequest request) {
        ticketService.create(request);
        return ResponseEntity.ok(ResponseHelper.ok("Success"));
    }

    @PutMapping(value = "/ticket", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> update(
            @Valid @RequestBody UpdateTicketRequest request) {
        ticketService.update(request);
        return ResponseEntity.ok(ResponseHelper.ok("Success"));
    }

    // @GetMapping(value = "/tickets/developer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<WebResponse<List<TicketResponse>>> findByUserId(@PathVariable String
    // id) {
    // return ResponseEntity.ok(ResponseHelper.ok(ticketService.getByUserId(id)));
    // }

    @GetMapping(value = "/ticket/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<TicketResponse>> findById(@PathVariable String id) {
        return ResponseEntity.ok(ResponseHelper.ok(ticketService.getById(id)));
    }

    @PutMapping(value = "/ticket/update-status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> updateStatus(
            @Valid @RequestBody ChangeStatusTicketRequest request) {
        ticketService.changeStatus(request);
        return ResponseEntity.ok(ResponseHelper.ok("Success"));
    }

    @PutMapping(value = "/ticket/assign", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> assignTicket(@RequestBody String ticketId,
            @RequestBody String userId) {
        ticketService.assignTicket(ticketId, userId);
        return ResponseEntity.ok(ResponseHelper.ok("Success"));
    }

    @PutMapping(value = "/ticket/process", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> processTicket(@RequestBody String ticketId) {
        ticketService.processTicket(ticketId);
        return ResponseEntity.ok(ResponseHelper.ok("Success"));
    }



    // @GetMapping(value = "/ticket/report", produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<WebResponse<File>> generateReport() throws JRException {
    // return ResponseEntity.ok(ResponseHelper.ok(ticketService.getReportTicket()));
    // }
}
