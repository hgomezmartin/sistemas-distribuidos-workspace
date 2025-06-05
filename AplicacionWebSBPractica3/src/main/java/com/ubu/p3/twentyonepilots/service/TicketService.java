package com.ubu.p3.twentyonepilots.service;

import com.ubu.p3.twentyonepilots.model.Ticket;
import com.ubu.p3.twentyonepilots.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * gestion de entradas de concierto
 */

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repo;

    public List<Ticket> list() {
        return repo.findAll();
    }

    public Ticket get(Long id)  {
        return repo.findById(id).orElseThrow();
    }

    public Ticket save(Ticket t) {
        return repo.save(t);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}