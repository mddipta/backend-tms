package com.lawencon.ticket.service.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Service;
import com.lawencon.ticket.model.request.transaction.CreateTicketTransactionRequest;
import com.lawencon.ticket.persistence.entity.TicketStatus;
import com.lawencon.ticket.persistence.entity.TicketTransaction;
import com.lawencon.ticket.persistence.repository.TicketTransactionRepository;
import com.lawencon.ticket.service.TicketStatusService;
import com.lawencon.ticket.service.TicketTransactionService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketTransactionServiceImpl implements TicketTransactionService {

    private final TicketTransactionRepository repository;

    private final TicketStatusService ticketStatusService;

    @Override
    public void create(CreateTicketTransactionRequest request) {
        TicketTransaction ticketTransaction = new TicketTransaction();
        ticketTransaction.setNumber(request.getNumber());

        if (request.getUser() != null) {
            ticketTransaction.setUser(request.getUser());
        }

        TicketStatus ticketStatus = ticketStatusService.getEntityByCode(request.getStatus());

        ticketStatus.setId(ticketStatus.getId());
        ticketTransaction.setTicketStatus(ticketStatus);
        ticketTransaction.setTicket(request.getTicket());

        repository.save(ticketTransaction);
    }

    @Override
    public TicketTransaction getLastByTicketId(String id) {
        if (repository.findTopByTicketIdOrderByCreatedAtDesc(id).isPresent()) {
            return repository.findTopByTicketIdOrderByCreatedAtDesc(id).get();
        } else {
            return null;
        }
    }

}
