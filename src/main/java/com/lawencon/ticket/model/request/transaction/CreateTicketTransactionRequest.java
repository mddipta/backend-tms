package com.lawencon.ticket.model.request.transaction;

import com.lawencon.ticket.persistence.entity.Ticket;
import com.lawencon.ticket.persistence.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketTransactionRequest {
    private Long number;
    private Ticket ticket;
    private String status;
    private String createdBy;
    private User user;
}
