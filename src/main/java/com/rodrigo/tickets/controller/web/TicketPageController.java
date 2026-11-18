package com.rodrigo.tickets.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.rodrigo.tickets.dto.CreateTicketActivityRequest;
import com.rodrigo.tickets.dto.CreateTicketRequest;
import com.rodrigo.tickets.dto.TicketViewDetail;
import com.rodrigo.tickets.entity.TicketCriticality;
import com.rodrigo.tickets.entity.TicketStatus;
import com.rodrigo.tickets.entity.User;
import com.rodrigo.tickets.exception.NoTicketActivityException;
import com.rodrigo.tickets.service.ProjectService;
import com.rodrigo.tickets.service.TicketActivityService;
import com.rodrigo.tickets.service.TicketService;
import com.rodrigo.tickets.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
@RequestMapping("/projects/{projectId}/tickets")
public class TicketPageController {

    private final TicketService ticketService;
    private final TicketActivityService activityService;
    private final UserService userService;
    private final ProjectService projectService;

    public TicketPageController(TicketService ticketService, TicketActivityService activityService, UserService userService,
        ProjectService projectService
    ) {
        this.ticketService = ticketService;
        this.activityService = activityService;
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping
    public String showTickets(@PathVariable Long projectId, Model model, Authentication authentication) {
        model.addAttribute("tickets", ticketService.findByProject(projectId));
        model.addAttribute("project", projectService.findById(projectId));
        User currentUser = userService.findByEmail(authentication.getName());
        model.addAttribute("currentUser", currentUser);
        return "tickets/list";
    }

    @GetMapping("/new")
    public String showCreateForm(@PathVariable Long projectId, Model model, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        CreateTicketRequest request = new CreateTicketRequest();

        request.setProjectId(projectId);

        model.addAttribute("ticket", request);
        model.addAttribute("project", projectService.findById(projectId));
        model.addAttribute("criticalities", TicketCriticality.values());
        model.addAttribute("currentUser", userService.findByEmail(authentication.getName()));
        model.addAttribute("owners", userService.findAllEnabled());
        model.addAttribute("currentUser", currentUser);

        return "tickets/form";
    }
    
    @GetMapping("/{ticketId}")
    public String showTicketDetail( @PathVariable Long projectId,
        @PathVariable Long ticketId,
        Model model,
        Authentication authentication) {
        TicketViewDetail ticket = ticketService.findById(ticketId);
        CreateTicketActivityRequest request = new CreateTicketActivityRequest();

        request.setStatus(ticket.status());
        request.setOwnerId(ticket.ownerId());

        User currentUser = userService.findByEmail(authentication.getName());
        
        model.addAttribute("ticket", ticket);
        model.addAttribute("project", projectService.findById(projectId));
        model.addAttribute("activities", activityService.findByTicketId(ticketId));
        model.addAttribute("newActivity", request);
        model.addAttribute("statuses", TicketStatus.values());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("owners", userService.findAllEnabled());

        return "tickets/detail";
    }
    
    @PostMapping
    public String createTicket(
            @PathVariable Long projectId,
            @Valid
            @ModelAttribute("ticket")
            CreateTicketRequest request,
            BindingResult bindingResult,
            Model model,
            Authentication authentication
    ) {

        request.setProjectId(projectId);

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                "project",
                projectService.findById(projectId)
            );

            model.addAttribute(
                "criticalities",
                TicketCriticality.values()
            );

            model.addAttribute(
                "owners",
                userService.findAllEnabled()
            );

            model.addAttribute(
                "currentUser",
                userService.findByEmail(
                    authentication.getName()
                )
            );

            return "tickets/form";
        }

        ticketService.create(request);

        return "redirect:/projects/"
            + projectId
            + "/tickets";
    }

    @PostMapping("/{id}/activities")
    public String createActivity(
            @PathVariable Long projectId,
            @PathVariable Long id,
            @Valid
            @ModelAttribute("newActivity")
            CreateTicketActivityRequest request,
            BindingResult bindingResult,
            Model model,
            Authentication authentication
    ) {

        if (bindingResult.hasErrors()) {
            prepareDetailsPage(
                id,
                model,
                authentication
            );

            return "tickets/detail";
        }

        User currentUser =
            userService.findByEmail(
                authentication.getName()
            );

        try {

            activityService.create(
                id,
                request,
                currentUser.getName()
            );

        } catch (NoTicketActivityException exception) {

            model.addAttribute(
                "activityError",
                exception.getMessage()
            );

            prepareDetailsPage(
                id,
                model,
                authentication
            );

            return "tickets/detail";
        }

        return "redirect:/projects/" + projectId + "/tickets/" + id;
    }
    
    
private void prepareDetailsPage(
        Long id,
        Model model,
        Authentication authentication
) {

    model.addAttribute(
        "ticket",
        ticketService.findById(id)
    );

    model.addAttribute(
        "activities",
        activityService.findByTicketId(id)
    );

    model.addAttribute(
        "statuses",
        TicketStatus.values()
    );

    model.addAttribute(
        "owners",
        userService.findAllEnabled()
    );

    model.addAttribute(
        "currentUser",
        userService.findByEmail(
            authentication.getName()
        )
    );
}
    
    
}
