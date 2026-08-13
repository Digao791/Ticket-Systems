package com.rodrigo.tickets.dto;

import java.time.LocalDate;

import com.rodrigo.tickets.entity.TicketStatus;

public record TicketViewDetail(Long id,
    String name,
    Long ownerId,
    String owner,
    LocalDate date,
    String description,
    String criticality,
    String criticalityCssClass,
    TicketStatus status,
    String statusCssClass) {
    
}
    