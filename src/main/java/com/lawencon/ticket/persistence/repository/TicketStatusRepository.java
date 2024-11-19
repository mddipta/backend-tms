package com.lawencon.ticket.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawencon.ticket.persistence.entity.TicketStatus;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, String> {
    TicketStatus findByCode(String code);
}
