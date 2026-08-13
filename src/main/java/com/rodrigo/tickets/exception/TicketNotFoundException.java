package com.rodrigo.tickets.exception;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(Long id) {
        super("Ticket with id " + id + " not found.");
    }
    
}
