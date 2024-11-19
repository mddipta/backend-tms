package com.lawencon.ticket.service;

import java.util.List;
import com.lawencon.ticket.model.request.ticket.ChangeStatusTicketRequest;
import com.lawencon.ticket.model.request.ticket.CreateTicketRequest;
import com.lawencon.ticket.model.request.ticket.UpdateTicketRequest;
import com.lawencon.ticket.model.response.File;
import com.lawencon.ticket.model.response.ticket.TicketResponse;
import com.lawencon.ticket.persistence.entity.Ticket;
import net.sf.jasperreports.engine.JRException;

public interface TicketService {
    List<TicketResponse> getAll();

    void create(CreateTicketRequest request);

    void update(UpdateTicketRequest request);

    List<TicketResponse> getByUserId(String id);

    TicketResponse getById(String id);

    Ticket getEntityById(String id);

    void changeStatus(ChangeStatusTicketRequest request);

    File getReportTicket() throws JRException;
}
