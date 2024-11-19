package com.lawencon.ticket.model.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    @NotBlank(message = "Comment cannot be empty")
    private String comment;

    @NotBlank(message = "Ticket cannot be empty")
    private String ticketId;

    @NotBlank(message = "User cannot be empty")
    private String userId;
}
