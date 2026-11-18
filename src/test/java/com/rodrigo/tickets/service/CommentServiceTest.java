package com.rodrigo.tickets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rodrigo.tickets.dto.CreateCommentRequest;
import com.rodrigo.tickets.entity.Comment;
import com.rodrigo.tickets.entity.Ticket;
import com.rodrigo.tickets.exception.TicketNotFoundException;
import com.rodrigo.tickets.repository.CommentRepository;
import com.rodrigo.tickets.repository.TicketRepository;

class CommentServiceTest {

    private CommentRepository commentRepository;
    private TicketRepository ticketRepository;
    private CommentService service;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        ticketRepository = mock(TicketRepository.class);
        service = new CommentService(commentRepository, ticketRepository);
    }

    @Test
    void findByTicketIdMapsCommentsToViews() {
        Comment comment = mock(Comment.class);
        when(comment.getAuthor()).thenReturn("Ana");
        when(comment.getContent()).thenReturn("Investigating");
        LocalDateTime createdAt = LocalDateTime.of(2026, 11, 18, 20, 30);
        when(comment.getCreatedAt()).thenReturn(createdAt);
        when(commentRepository.findByTicketIdOrderByCreatedAtAsc(10L))
            .thenReturn(List.of(comment));

        var result = service.findByTicketId(10L);

        assertEquals(1, result.size());
        assertEquals("Ana", result.get(0).author());
        assertEquals("Investigating", result.get(0).content());
        assertEquals(createdAt, result.get(0).createdAt());
    }

    @Test
    void createSavesCommentForExistingTicket() {
        Ticket ticket = mock(Ticket.class);
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        CreateCommentRequest request = new CreateCommentRequest();
        request.setAuthor("Ana");
        request.setContent("Root cause found");

        service.create(10L, request);

        verify(commentRepository).save(org.mockito.ArgumentMatchers.argThat(comment ->
            comment.getTicket() == ticket
                && "Ana".equals(comment.getAuthor())
                && "Root cause found".equals(comment.getContent())
                && comment.getCreatedAt() != null
        ));
    }

    @Test
    void createThrowsWhenTicketDoesNotExist() {
        when(ticketRepository.findById(404L)).thenReturn(Optional.empty());
        CreateCommentRequest request = new CreateCommentRequest();
        request.setAuthor("Ana");
        request.setContent("Cannot add");

        assertThrows(TicketNotFoundException.class, () -> service.create(404L, request));
    }
}
