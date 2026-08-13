package com.rodrigo.tickets.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigo.tickets.dto.ProjectCardView;
import com.rodrigo.tickets.entity.Project;
import com.rodrigo.tickets.entity.TicketActivity;
import com.rodrigo.tickets.entity.TicketStatus;
import com.rodrigo.tickets.entity.User;
import com.rodrigo.tickets.entity.UserRole;
import com.rodrigo.tickets.repository.ProjectMemberRepository;
import com.rodrigo.tickets.repository.ProjectRepository;
import com.rodrigo.tickets.repository.TicketActivityRepository;
import com.rodrigo.tickets.repository.TicketRepository;


@Service
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TicketRepository ticketRepository;
    private final TicketActivityRepository ticketActivityRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            ProjectMemberRepository projectMemberRepository,
            TicketRepository ticketRepository,
            TicketActivityRepository ticketActivityRepository
    ) {
        this.projectRepository =
            projectRepository;

        this.projectMemberRepository =
            projectMemberRepository;

        this.ticketRepository =
            ticketRepository;

        this.ticketActivityRepository =
            ticketActivityRepository;
    }

    @Transactional(readOnly = true)
    public List<ProjectCardView> findProjectsForHome
    (
        User user
    ){
        List<Project> projects;
        if(user.getRole() == UserRole.ADMIN){
            projects = projectRepository.findAll();
        }else{

            projects = projectMemberRepository
                        .findByUserId(user.getId())
                        .stream()
                        .map(member -> member.getProject())
                        .toList();
        }

        return projects
                .stream()
                .filter(Project::isActive)
                .map(project -> toCard(project, user))
                .toList();
    }

    @Transactional(readOnly = true)
    public Project findById(Long id){
        return projectRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    private ProjectCardView toCard(Project project, User user){
        List<TicketStatus> activeStatuses = List.of(TicketStatus.OPEN, TicketStatus.IN_PROGRESS);

        long openTickets =
            ticketRepository
                .countActiveTicketsByProject(
                    project.getId(),
                    activeStatuses
                );

        long myTickets =
            ticketRepository
                .countActiveTicketsByProjectAndOwner(
                    project.getId(),
                    user.getId(),
                    activeStatuses
                );

        LocalDateTime lastActivity = ticketActivityRepository.findByProjectOrderByCreatedAtDesc(
            project.getId(), PageRequest.of(0, 1)
        ).stream().findFirst().map(TicketActivity::getCreatedAt)
        .orElse(null);

        return new ProjectCardView(project.getId(), project.getName(), openTickets, myTickets, lastActivity);
        
    }
        
}
