package com.lawencon.ticket.model.request.ticket;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignDeveloperTicketRequest {
    @NotBlank(message = "Ticket ID cannot be empty")
    private String ticketId;

    @NotBlank(message = "Developer ID cannot be empty")
    private String userId;
}
