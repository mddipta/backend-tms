package com.lawencon.ticket.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.ticket.helper.ResponseHelper;
import com.lawencon.ticket.model.response.WebResponse;
import com.lawencon.ticket.model.response.priorityticketstatus.PriorityTicketStatusResponse;
import com.lawencon.ticket.service.PriorityTicketStatusService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Priority Ticket Status", description = "Priority Ticket Status API endpoint")
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class PriorityTicketController {

    private final PriorityTicketStatusService priorityTicketStatusService;

    @GetMapping(value = "/priority-tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<PriorityTicketStatusResponse>>> findAll() {
        return ResponseEntity.ok(ResponseHelper.ok(priorityTicketStatusService.getAll()));
    }
}
