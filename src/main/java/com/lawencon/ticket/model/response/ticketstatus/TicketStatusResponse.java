package com.lawencon.ticket.model.response.ticketstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatusResponse {
    private Long id;
    private String code;
    private String name;
    private Long version;
}
