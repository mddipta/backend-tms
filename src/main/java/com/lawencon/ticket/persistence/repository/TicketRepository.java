package com.lawencon.ticket.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawencon.ticket.persistence.entity.Ticket;


public interface TicketRepository extends JpaRepository<Ticket, String> {

    List<Ticket> findByUserId(String id);

    List<Ticket> findByCustomerId(String id);
}
