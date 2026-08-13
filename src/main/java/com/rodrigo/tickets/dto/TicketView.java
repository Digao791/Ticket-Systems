package com.rodrigo.tickets.dto;

import java.time.LocalDate;

public record TicketView(
    Long id,
    String name,
    String owner,
    LocalDate date,
    String criticality,
    String criticalityCssClass,
    String status,
    String statusCssClass
) {
    
}
