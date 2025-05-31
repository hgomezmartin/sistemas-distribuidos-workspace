package com.ubu.p3.twentyonepilots.repository;

import com.ubu.p3.twentyonepilots.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
