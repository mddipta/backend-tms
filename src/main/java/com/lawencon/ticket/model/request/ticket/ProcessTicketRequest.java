package com.lawencon.ticket.model.request.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessTicketRequest {
    @NotBlank(message = "Ticket ID cannot be empty")
    private String ticketId;
}
