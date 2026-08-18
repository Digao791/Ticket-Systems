package com.rodrigo.tickets.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rodrigo.tickets.dto.CreateTicketActivityRequest;
import com.rodrigo.tickets.entity.Ticket;
import com.rodrigo.tickets.entity.TicketStatus;
import com.rodrigo.tickets.entity.User;
import com.rodrigo.tickets.exception.NoTicketActivityException;
import com.rodrigo.tickets.repository.TicketActivityRepository;
import com.rodrigo.tickets.repository.TicketRepository;
import com.rodrigo.tickets.repository.UserRepository;
import com.rodrigo.tickets.service.TicketActivityService;
import com.rodrigo.tickets.service.UserService;

public class TicketActivityServiceTest {
    
    private TicketRepository ticketRepository;
    private TicketActivityRepository activityRepository;
    private UserRepository userRepository;
    private UserService userService;
    private TicketActivityService service;

    @BeforeEach
    void setUp(){
        ticketRepository = mock(TicketRepository.class);
        activityRepository = mock(TicketActivityRepository.class);
        userRepository = mock(UserRepository.class);
        userService = mock(UserService.class);

        service = new TicketActivityService(activityRepository, ticketRepository, userService, userRepository);
    }

    @Test
    void shouldNotCreateActivityWhenNothingChanged(){
        User owner = mock(User.class);
        when(owner.getId()).thenReturn(1L);
        Ticket ticket = mock(Ticket.class);
        when(ticket.getOwner()).thenReturn(owner);
        when(ticket.getStatus()).thenReturn(TicketStatus.OPEN);

        CreateTicketActivityRequest request = new CreateTicketActivityRequest();
        request.setOwnerId(1L);
        request.setStatus(TicketStatus.OPEN);
        request.setComment("");

        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));

        assertThrows(NoTicketActivityException.class, () -> service.create(
            10L, request, "admin@tickets.local"
        ));

        verify(activityRepository, never()).save(any());
    }
}
