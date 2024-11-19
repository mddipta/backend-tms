package com.lawencon.ticket.model.response.ticket;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private String id;
    private String code;
    private String title;
    private LocalDate dueDate;
    private LocalDate date;
    private String priorityTicket;
    private String status;
    private String pic;
    private String developer;
    private Long version;
}
