package com.lawencon.ticket.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lawencon.ticket.helper.ResponseHelper;
import com.lawencon.ticket.model.request.comment.CreateCommentRequest;
import com.lawencon.ticket.model.response.WebResponse;
import com.lawencon.ticket.model.response.comment.CommentResponse;
import com.lawencon.ticket.service.CommentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "Comment", description = "Comment API endpoint")
@RestController
@RequestMapping({"/api/v1"})
@AllArgsConstructor
public class CommentController {
    final private CommentService service;

    @PostMapping(value = "/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> add(@Valid @RequestBody CreateCommentRequest request) {
        service.create(request);
        return ResponseEntity.ok("Success");
    }

    @GetMapping(value = "/comments/{id}/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<CommentResponse>>> findByTicketId(
            @PathVariable String id) {
        return ResponseEntity.ok(ResponseHelper.ok(service.findByTicketId(id)));
    }
}
