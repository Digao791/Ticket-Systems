package com.rodrigo.tickets.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.rodrigo.tickets.database.DatabaseStatusService;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DatabaseErrorController {
    
    private final DatabaseStatusService databaseStatusService;

    public DatabaseErrorController(DatabaseStatusService databaseStatusService){
        this.databaseStatusService = databaseStatusService;
    }

    @GetMapping("/database-error")
    public String databaseError(Model model) {
        model.addAttribute("databaseStatus", databaseStatusService.getStatus());
        return "error/database-error";
    }
    
}
