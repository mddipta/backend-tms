package com.lawencon.ticket.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lawencon.ticket.model.response.priorityticketstatus.PriorityTicketStatusResponse;
import com.lawencon.ticket.persistence.entity.PriorityTicketStatus;
import com.lawencon.ticket.persistence.repository.PriorityTicketStatusRepository;
import com.lawencon.ticket.service.PriorityTicketStatusService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PriorityTicketStatusServiceImpl implements PriorityTicketStatusService {

    private final PriorityTicketStatusRepository repository;

    @Override
    public List<PriorityTicketStatusResponse> getAll() {
        List<PriorityTicketStatusResponse> responses = new ArrayList<>();
        List<PriorityTicketStatus> priorityTicketStatuses = repository.findAll();
        for (PriorityTicketStatus priorityTicketStatus : priorityTicketStatuses) {
            PriorityTicketStatusResponse response = mapToResponse(priorityTicketStatus);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public PriorityTicketStatusResponse getByCode(String code) {
        Optional<PriorityTicketStatus> priorityTicket = repository.findByCode(code);
        if (priorityTicket.isPresent()) {
            return mapToResponse(priorityTicket.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public PriorityTicketStatus getEntityByCode(String code) {
        Optional<PriorityTicketStatus> priorityTicket = repository.findByCode(code);
        if (priorityTicket.isPresent()) {
            return priorityTicket.get();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    private PriorityTicketStatusResponse mapToResponse(PriorityTicketStatus priorityTicketStatus) {
        PriorityTicketStatusResponse response = new PriorityTicketStatusResponse();
        BeanUtils.copyProperties(priorityTicketStatus, response);
        return response;
    }



}
