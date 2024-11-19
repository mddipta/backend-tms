package com.lawencon.ticket.service;

import java.util.List;

import com.lawencon.ticket.model.request.comment.CreateCommentRequest;
import com.lawencon.ticket.model.response.comment.CommentResponse;

public interface CommentService {
    void create(CreateCommentRequest request);

    List<CommentResponse> findByTicketId(String id);
}
