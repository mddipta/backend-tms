package com.lawencon.ticket.service;

import java.util.List;

import com.lawencon.ticket.model.response.priorityticketstatus.PriorityTicketStatusResponse;
import com.lawencon.ticket.persistence.entity.PriorityTicketStatus;

public interface PriorityTicketStatusService {
    List<PriorityTicketStatusResponse> getAll();

    PriorityTicketStatusResponse getByCode(String code);

    PriorityTicketStatus getEntityByCode(String code);

    PriorityTicketStatus getEntityById(String id);
}
