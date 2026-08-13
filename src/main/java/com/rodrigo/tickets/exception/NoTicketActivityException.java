package com.rodrigo.tickets.exception;

public class NoTicketActivityException extends RuntimeException{
    
    public NoTicketActivityException(){
        super("Nenhuma alteração foi realizada.");
    }
}
