package com.lawencon.ticket.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_tickets_trx")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketTransaction extends AuditableEntity {
    @Column(name = "number", nullable = false)
    private Long number;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "status_ticket_id", nullable = false)
    private TicketStatus ticketStatus;

}
