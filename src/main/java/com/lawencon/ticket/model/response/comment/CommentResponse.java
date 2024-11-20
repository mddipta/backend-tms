package com.lawencon.ticket.model.response.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private String comment;
    private String name;
    private String userId;
    private String ticketId;
    private String date;
}
