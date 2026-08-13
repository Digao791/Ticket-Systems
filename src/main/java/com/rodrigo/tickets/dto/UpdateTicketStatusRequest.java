package com.rodrigo.tickets.dto;

import com.rodrigo.tickets.entity.TicketStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateTicketStatusRequest {
    
    @NotNull
    private TicketStatus status;

    public UpdateTicketStatusRequest() {
        // Default constructor for deserialization
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}
