package com.lawencon.ticket.service;

import java.util.List;
import org.springframework.data.domain.Page;
import com.lawencon.ticket.model.request.PagingRequest;
import com.lawencon.ticket.model.request.ticket.AssignDeveloperTicketRequest;
import com.lawencon.ticket.model.request.ticket.ChangeStatusTicketRequest;
import com.lawencon.ticket.model.request.ticket.CreateTicketRequest;
import com.lawencon.ticket.model.request.ticket.ProcessTicketRequest;
import com.lawencon.ticket.model.request.ticket.UpdateTicketRequest;
import com.lawencon.ticket.model.response.File;
import com.lawencon.ticket.model.response.ticket.TicketResponse;
import com.lawencon.ticket.persistence.entity.Ticket;
import net.sf.jasperreports.engine.JRException;

public interface TicketService {
    Page<TicketResponse> getAll(PagingRequest pagingRequest, String inquiry);

    void create(CreateTicketRequest request);

    void update(UpdateTicketRequest request);

    List<TicketResponse> getByUserId(String id);

    Page<TicketResponse> getByUser(PagingRequest pagingRequest, String inquiry);

    // List<TicketResponse> getByCustomerId(String id);

    TicketResponse getById(String id);

    Ticket getEntityById(String id);

    void changeStatus(ChangeStatusTicketRequest request);

    void assignTicket(AssignDeveloperTicketRequest request);

    void processTicket(ProcessTicketRequest request);

    void finishTicket(ProcessTicketRequest id);

    // File getReportTicket() throws JRException;
}
