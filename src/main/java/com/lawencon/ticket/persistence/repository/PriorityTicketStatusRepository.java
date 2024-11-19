package com.lawencon.ticket.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawencon.ticket.persistence.entity.PriorityTicketStatus;

public interface PriorityTicketStatusRepository
        extends JpaRepository<PriorityTicketStatus, String> {
    Optional<PriorityTicketStatus> findByCode(String code);
}
