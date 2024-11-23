package com.lawencon.ticket.service;

import java.util.List;

import com.lawencon.ticket.model.response.ticketstatus.TicketStatusResponse;
import com.lawencon.ticket.persistence.entity.TicketStatus;

public interface TicketStatusService {
    List<TicketStatusResponse> getAll();

    TicketStatus getEntityById(String id);

    TicketStatus getEntityByCode(String code);

    List<TicketStatusResponse> getStatusForCustomer();
}
