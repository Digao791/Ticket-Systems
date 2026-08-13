package com.rodrigo.tickets.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigo.tickets.dto.CreateTicketRequest;
import com.rodrigo.tickets.dto.TicketView;
import com.rodrigo.tickets.dto.TicketViewDetail;
import com.rodrigo.tickets.entity.Project;
import com.rodrigo.tickets.entity.Ticket;
import com.rodrigo.tickets.entity.User;
import com.rodrigo.tickets.exception.TicketNotFoundException;
import com.rodrigo.tickets.repository.ProjectRepository;
import com.rodrigo.tickets.repository.TicketRepository;
import com.rodrigo.tickets.repository.UserRepository;


@Service
public class TicketService {
    
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, ProjectRepository 
        projectRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }   

    @Transactional(readOnly = true)
    public List<TicketView> findAllForView(){
        return ticketRepository.findAllByOrderByCreatedAtDesc()
        .stream().map(this::toView).toList();
    }

    @Transactional(readOnly = true)
    public TicketViewDetail findById(Long id){
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
        return toDetailView(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketView> findByProject(
            Long projectId
    ) {
        return ticketRepository
            .findByProject_IdOrderByCreatedAtDesc(projectId)
            .stream()
            .map(this::toView)
            .toList();
    }

    @Transactional
    public Ticket create(CreateTicketRequest request){
        User owner = userRepository.findById(request.getOwnerId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid Owner"));

        Project project = projectRepository.findById(request.getProjectId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid project"));
        
        Ticket ticket = new Ticket(
            request.getName(),
            project,
            owner,
            request.getDescription(),
            LocalDate.now(),
            request.getCriticality()
        );
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketViewDetail> findCommentsByTicketId(Long ticketId){
        return List.of(); // Placeholder for actual implementation
    }

    private TicketView toView(Ticket ticket){
        return new TicketView(ticket.getId(), 
        ticket.getName(), 
        ticket.getOwner().getName(), 
        ticket.getCreatedAt(), 
        ticket.getCriticality().getLabel(), 
        ticket.getCriticality().getCssClass(),
        ticket.getStatus().getLabel(),
        ticket.getStatus().getCssClass());
    }

    private TicketViewDetail toDetailView(Ticket ticket){
        return new TicketViewDetail(ticket.getId(), 
        ticket.getName(), 
        ticket.getOwner().getId(),
        ticket.getOwner().getName(), 
        ticket.getCreatedAt(), 
        ticket.getDescription(), 
        ticket.getCriticality().getLabel(), 
        ticket.getCriticality().getCssClass(),
        ticket.getStatus(),
        ticket.getStatus().getCssClass()
        );
    }
}
