package com.rodrigo.tickets.repository;

import java.util.Collection;
import java.util.List;

import com.rodrigo.tickets.entity.Ticket;
import com.rodrigo.tickets.entity.TicketStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository
        extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByOrderByCreatedAtDesc();


    @Query("""
        SELECT COUNT(t)
        FROM Ticket t
        WHERE t.project.id = :projectId
          AND t.status IN :statuses
    """)
    long countActiveTicketsByProject(
        @Param("projectId") Long projectId,
        @Param("statuses") Collection<TicketStatus> statuses
    );


    @Query("""
        SELECT COUNT(t)
        FROM Ticket t
        WHERE t.project.id = :projectId
          AND t.ownerId.id = :ownerId
          AND t.status IN :statuses
    """)
    long countActiveTicketsByProjectAndOwner(
        @Param("projectId") Long projectId,
        @Param("ownerId") Long ownerId,
        @Param("statuses") Collection<TicketStatus> statuses
    );

    List<Ticket> findByProject_IdOrderByCreatedAtDesc(
    Long projectId
);
}