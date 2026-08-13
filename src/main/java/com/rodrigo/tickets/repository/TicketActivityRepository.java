package com.rodrigo.tickets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.tickets.entity.TicketActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketActivityRepository extends JpaRepository<TicketActivity, Long> {
    
    List<TicketActivity> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
   @Query("""
    SELECT ta
    FROM TicketActivity ta
    WHERE ta.ticket.project.id = :projectId
    ORDER BY ta.createdAt DESC
    """)
    List<TicketActivity> findByProjectOrderByCreatedAtDesc(
        @Param("projectId") Long projectId, Pageable pageable
    );
}
