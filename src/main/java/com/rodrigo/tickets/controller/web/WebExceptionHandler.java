package com.rodrigo.tickets.controller.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rodrigo.tickets.exception.TicketNotFoundException;

@ControllerAdvice   
public class WebExceptionHandler {
    
    @ExceptionHandler(TicketNotFoundException.class)
    public String handleTicketNotFoundException(TicketNotFoundException ex, Model model) 
    {
        model.addAttribute("message", ex.getMessage());
        return "error/ticket-not-found";
    }
}
