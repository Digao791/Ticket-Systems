package com.rodrigo.tickets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rodrigo.tickets.entity.Project;
import com.rodrigo.tickets.entity.ProjectMember;
import com.rodrigo.tickets.entity.User;
import com.rodrigo.tickets.entity.UserRole;
import com.rodrigo.tickets.repository.ProjectMemberRepository;
import com.rodrigo.tickets.repository.ProjectRepository;
import com.rodrigo.tickets.repository.TicketActivityRepository;
import com.rodrigo.tickets.repository.TicketRepository;

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectMemberRepository projectMemberRepository;
    private TicketRepository ticketRepository;
    private TicketActivityRepository ticketActivityRepository;
    private ProjectService service;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectMemberRepository = mock(ProjectMemberRepository.class);
        ticketRepository = mock(TicketRepository.class);
        ticketActivityRepository = mock(TicketActivityRepository.class);
        service = new ProjectService(
            projectRepository,
            projectMemberRepository,
            ticketRepository,
            ticketActivityRepository
        );
    }

    @Test
    void findProjectsForHomeReturnsAllActiveProjectsForAdmin() {
        User admin = mock(User.class);
        when(admin.getRole()).thenReturn(UserRole.ADMIN);
        Project active = mock(Project.class);
        Project inactive = mock(Project.class);
        when(active.isActive()).thenReturn(true);
        when(inactive.isActive()).thenReturn(false);
        when(active.getId()).thenReturn(1L);
        when(active.getName()).thenReturn("Support");
        when(admin.getId()).thenReturn(100L);
        when(projectRepository.findAll()).thenReturn(List.of(active, inactive));
        when(ticketRepository.countActiveTicketsByProject(any(), any())).thenReturn(2L);
        when(ticketRepository.countActiveTicketsByProjectAndOwner(any(), any(), any())).thenReturn(1L);
        when(ticketActivityRepository.findByProjectOrderByCreatedAtDesc(any(), any()))
            .thenReturn(List.of());

        var result = service.findProjectsForHome(admin);

        assertEquals(1, result.size());
        assertEquals("Support", result.get(0).name());
    }

    @Test
    void findProjectsForHomeReturnsOnlyMemberProjectsForRegularUser() {
        User user = mock(User.class);
        when(user.getRole()).thenReturn(UserRole.AGENT);
        when(user.getId()).thenReturn(7L);

        Project memberProject = mock(Project.class);
        when(memberProject.isActive()).thenReturn(true);
        when(memberProject.getId()).thenReturn(2L);
        when(memberProject.getName()).thenReturn("Billing");
        ProjectMember member = mock(ProjectMember.class);
        when(member.getProject()).thenReturn(memberProject);
        when(projectMemberRepository.findByUserId(7L)).thenReturn(List.of(member));
        when(ticketRepository.countActiveTicketsByProject(any(), any())).thenReturn(0L);
        when(ticketRepository.countActiveTicketsByProjectAndOwner(any(), any(), any())).thenReturn(0L);
        when(ticketActivityRepository.findByProjectOrderByCreatedAtDesc(any(), any()))
            .thenReturn(List.of());

        var result = service.findProjectsForHome(user);

        assertEquals(1, result.size());
        assertEquals("Billing", result.get(0).name());
    }

    @Test
    void createSavesAndFlushesProject() {
        Project project = mock(Project.class);
        when(projectRepository.saveAndFlush(any(Project.class))).thenReturn(project);

        assertEquals(project, service.create("New project", "Description"));
    }

    @Test
    void findByIdThrowsForUnknownProject() {
        when(projectRepository.findById(404L)).thenReturn(java.util.Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(404L));
    }
}
