package com.rodrigo.tickets.controller.web;

import com.rodrigo.tickets.database.DatabaseStatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
    
    private final DatabaseStatusService databaseStatusService;

    LoginController(DatabaseStatusService databaseStatusService) {
        this.databaseStatusService = databaseStatusService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("databaseConnected", databaseStatusService.isConnected());
        return "login";
    }
    
}
