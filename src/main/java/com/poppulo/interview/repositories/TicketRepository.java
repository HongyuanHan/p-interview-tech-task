package com.poppulo.interview.repositories;

import com.poppulo.interview.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("select t from Ticket t where t.ticketIdentifier = ?1")
    Optional<Ticket> findOneByTicketIdentifier(String ticketIdentifier);
}
