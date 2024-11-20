package com.lawencon.ticket.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lawencon.ticket.model.request.comment.CreateCommentRequest;
import com.lawencon.ticket.model.response.comment.CommentResponse;
import com.lawencon.ticket.persistence.entity.Comment;
import com.lawencon.ticket.persistence.entity.Ticket;
import com.lawencon.ticket.persistence.entity.User;
import com.lawencon.ticket.persistence.repository.CommentRepository;
import com.lawencon.ticket.service.CommentService;
import com.lawencon.ticket.service.TicketService;
import com.lawencon.ticket.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    CommentRepository repository;
    TicketService ticketService;
    UserService userService;

    @Override
    public void create(CreateCommentRequest request) {
        Comment comment = new Comment();
        comment.setComment(request.getComment());

        Ticket ticket = ticketService.getEntityById(request.getTicketId());
        comment.setTicket(ticket);

        User user = userService.findEntityById(request.getUserId());
        comment.setUser(user);

        repository.save(comment);
    }

    @Override
    public List<CommentResponse> findByTicketId(String id) {
        List<CommentResponse> responses = new ArrayList<>();
        List<Comment> comments = repository.findByTicketId(id);
        for (Comment comment : comments) {
            CommentResponse response = mapToResponse(comment);
            responses.add(response);
        }
        return responses;
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setComment(comment.getComment());
        response.setName(comment.getUser().getName());
        response.setUserId(comment.getUser().getId());

        LocalDateTime localDateTime =
                comment.getCreatedAt().toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
        String formattedDate =
                localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd"));
        response.setDate(formattedDate);

        return response;
    }

}
