package com.lawencon.ticket.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.lawencon.ticket.persistence.entity.Ticket;
import org.springframework.data.domain.Pageable;

@Repository
public interface TicketRepository
        extends JpaRepository<Ticket, String>, JpaSpecificationExecutor<Ticket> {
    Page<Ticket> findByUserId(String userId, Specification<Ticket> spec, Pageable pageable);

    Page<Ticket> findByCustomerId(String customerId, Specification<Ticket> spec, Pageable pageable);

}
