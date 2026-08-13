package com.rodrigo.tickets.dto;

import java.time.LocalDateTime;

public record TicketActivityView(
    String author,
    String comment,

    String oldStatus,
    String newStatus,

    String oldOwner,
    String newOwner,

    boolean statusChanged,
    boolean ownerChanged,

    LocalDateTime createdAt
) {
    
}
