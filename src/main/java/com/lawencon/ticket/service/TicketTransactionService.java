package com.lawencon.ticket.service;

import com.lawencon.ticket.model.request.transaction.CreateTicketTransactionRequest;
import com.lawencon.ticket.persistence.entity.TicketTransaction;

public interface TicketTransactionService {
    void create(CreateTicketTransactionRequest request);

    TicketTransaction getLastByTicketId(String id);
}
