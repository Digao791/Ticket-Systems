package com.rodrigo.tickets.exception;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(
        {CannotCreateTransactionException.class,
            DataAccessResourceFailureException.class
        }
    )
    public String handleDatabaseUnavailable(){
        return "redirect:/database-error";
    }
}
