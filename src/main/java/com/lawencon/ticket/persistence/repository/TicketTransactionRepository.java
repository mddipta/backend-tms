package com.lawencon.ticket.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lawencon.ticket.persistence.entity.TicketTransaction;


public interface TicketTransactionRepository extends JpaRepository<TicketTransaction, String> {
    Optional<TicketTransaction> findTopByTicketIdOrderByCreatedAtDesc(String id);
}
