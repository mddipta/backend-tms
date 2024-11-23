package com.lawencon.ticket.model.request.ticket;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketRequest {
    @NotBlank(message = "Id cannot be empty")
    private String id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Priority Ticket Status cannot be empty")
    private String priorityTicketStatus;

    @NotBlank(message = "Is Active cannot be empty")
    private Boolean isActive;

    @NotBlank(message = "Due Date cannot be empty")
    private LocalDate dueDate;

    @NotBlank(message = "Status cannot be empty")
    private String status;

    @NotBlank(message = "Version cannot be empty")
    private Long version;

}
