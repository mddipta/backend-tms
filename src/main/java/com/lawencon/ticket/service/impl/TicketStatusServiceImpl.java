package com.lawencon.ticket.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.lawencon.ticket.model.response.ticketstatus.TicketStatusResponse;
import com.lawencon.ticket.persistence.entity.TicketStatus;
import com.lawencon.ticket.persistence.repository.TicketStatusRepository;
import com.lawencon.ticket.service.TicketStatusService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketStatusServiceImpl implements TicketStatusService {
    TicketStatusRepository repository;


    @Override
    public List<TicketStatusResponse> getAll() {
        List<TicketStatusResponse> responses = new ArrayList<>();
        List<TicketStatus> statuses = repository.findAll();
        for (TicketStatus ticket : statuses) {
            TicketStatusResponse response = mapToResponse(ticket);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public TicketStatus getEntityById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public TicketStatus getEntityByCode(String code) {
        return repository.findByCode(code);
    }

    private TicketStatusResponse mapToResponse(TicketStatus ticketStatus) {
        TicketStatusResponse response = new TicketStatusResponse();
        BeanUtils.copyProperties(ticketStatus, response);
        return response;
    }


}
