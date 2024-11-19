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

public class ChangeStatusTicketRequest {
    @NotBlank(message = "ID cannot be empty")
    private String id;

    @NotBlank(message = "Status cannot be empty")
    private String status;

    @Null
    private String userId;

    @NotBlank(message = "Version cannot be empty")
    private Boolean isActive;

    @NotBlank(message = "Version cannot be empty")
    private Long version;
}
