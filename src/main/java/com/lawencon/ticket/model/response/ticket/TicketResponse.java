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
    private String priorityTicketId;
    private String priorityTicket;
    private String statusId;
    private String statusName;
    private String pic;
    private String developer;
    private Long version;
    private String description;
}
