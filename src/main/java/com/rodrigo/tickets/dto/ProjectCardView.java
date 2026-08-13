package com.rodrigo.tickets.dto;

import java.time.LocalDateTime;

public record ProjectCardView(
    Long id,
    String name,
    long openTickets,
    long myTickets,
    LocalDateTime lastActivity
) {
    
}
