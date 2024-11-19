package com.lawencon.ticket.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.ticket.helper.ResponseHelper;
import com.lawencon.ticket.model.response.WebResponse;
import com.lawencon.ticket.model.response.ticketstatus.TicketStatusResponse;
import com.lawencon.ticket.service.TicketStatusService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Ticket Status", description = "Ticket Status API endpoint")
@RestController
@RequestMapping({"/api/v1"})
@AllArgsConstructor
public class TicketStatusController {
    private final TicketStatusService ticketStatusService;

    @GetMapping(value = "/ticket-statuses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<TicketStatusResponse>>> findAll() {
        return ResponseEntity.ok(ResponseHelper.ok(ticketStatusService.getAll()));
    }
}
