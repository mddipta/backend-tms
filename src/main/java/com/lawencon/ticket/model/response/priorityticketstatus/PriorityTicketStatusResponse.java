package com.lawencon.ticket.model.response.priorityticketstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriorityTicketStatusResponse {
    private String id;
    private String code;
    private String name;
    private Long version;
}
