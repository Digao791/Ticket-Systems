package com.rodrigo.tickets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rodrigo.tickets.dto.CreateTicketRequest;
import com.rodrigo.tickets.entity.Project;
import com.rodrigo.tickets.entity.Ticket;
import com.rodrigo.tickets.entity.TicketCriticality;
import com.rodrigo.tickets.entity.User;
import com.rodrigo.tickets.exception.TicketNotFoundException;
import com.rodrigo.tickets.repository.ProjectRepository;
import com.rodrigo.tickets.repository.TicketRepository;
import com.rodrigo.tickets.repository.UserRepository;

class TicketServiceTest {

    private TicketRepository ticketRepository;
    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private TicketService service;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        userRepository = mock(UserRepository.class);
        projectRepository = mock(ProjectRepository.class);
        service = new TicketService(ticketRepository, userRepository, projectRepository);
    }

    @Test
    void createSavesTicketWithRequestData() {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setName("Login fails");
        request.setOwnerId(7L);
        request.setProjectId(3L);
        request.setDescription("Unable to authenticate");
        request.setCriticality(TicketCriticality.HIGH);

        User owner = mock(User.class);
        Project project = mock(Project.class);
        when(userRepository.findById(7L)).thenReturn(Optional.of(owner));
        when(projectRepository.findById(3L)).thenReturn(Optional.of(project));

        Ticket saved = mock(Ticket.class);
        when(ticketRepository.save(org.mockito.ArgumentMatchers.any(Ticket.class))).thenReturn(saved);

        Ticket result = service.create(request);

        assertSame(saved, result);
        verify(ticketRepository).save(org.mockito.ArgumentMatchers.argThat(ticket ->
            "Login fails".equals(ticket.getName())
                && ticket.getOwner() == owner
                && ticket.getProject() == project
                && "Unable to authenticate".equals(ticket.getDescription())
                && ticket.getCriticality() == TicketCriticality.HIGH
        ));
    }

    @Test
    void createRejectsUnknownOwner() {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setOwnerId(99L);
        request.setProjectId(3L);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> service.create(request));

        assertEquals("Invalid Owner", exception.getMessage());
    }

    @Test
    void createRejectsUnknownProject() {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setOwnerId(7L);
        request.setProjectId(99L);
        when(userRepository.findById(7L)).thenReturn(Optional.of(mock(User.class)));
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> service.create(request));

        assertEquals("Invalid project", exception.getMessage());
    }

    @Test
    void findByIdThrowsWhenTicketDoesNotExist() {
        when(ticketRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> service.findById(123L));
    }
}
