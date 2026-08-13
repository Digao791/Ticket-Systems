package com.rodrigo.tickets.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rodrigo.tickets.entity.User;
import com.rodrigo.tickets.entity.UserRole;
import com.rodrigo.tickets.service.ProjectService;
import com.rodrigo.tickets.service.UserService;


@Controller
public class HomeController {

    private final UserService userService;
    private final ProjectService projectService;

    public HomeController(UserService userService, ProjectService projectService){
        this.userService = userService;
        this.projectService = projectService;
    }
    
    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("projects", projectService.findProjectsForHome(currentUser));
        model.addAttribute("isAdmin", currentUser.getRole() == UserRole.ADMIN);
        return "index";
    }
    
}
