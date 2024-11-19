package com.lawencon.ticket.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawencon.ticket.persistence.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Object> {
    List<Comment> findByTicketId(String id);
}
