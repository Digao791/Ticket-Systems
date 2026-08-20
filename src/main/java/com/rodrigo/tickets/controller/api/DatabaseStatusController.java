package com.rodrigo.tickets.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.tickets.database.DatabaseStatus;
import com.rodrigo.tickets.database.DatabaseStatusService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/database")
public class DatabaseStatusController {
    
    private final DatabaseStatusService databaseStatusService;

    public DatabaseStatusController(DatabaseStatusService databaseStatusService){
        this.databaseStatusService = databaseStatusService;
    }

    @GetMapping("/status")
    public DatabaseStatus status() {
        return databaseStatusService.getStatus();
    }
    
}
