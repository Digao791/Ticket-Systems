package com.rodrigo.tickets.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rodrigo.tickets.dto.CommentView;
import com.rodrigo.tickets.dto.CreateCommentRequest;
import com.rodrigo.tickets.entity.Comment;
import com.rodrigo.tickets.entity.Ticket;
import com.rodrigo.tickets.exception.TicketNotFoundException;
import com.rodrigo.tickets.repository.CommentRepository;
import com.rodrigo.tickets.repository.TicketRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;

    public CommentService(CommentRepository commentRepository, TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
    }
    
    @Transactional
    public List<CommentView> findByTicketId(Long ticketId){
        return commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId)
                .stream()
                .map(this::mapToCommentView)
                .toList();
    }

    @Transactional
    public void create(Long ticketId, CreateCommentRequest request){
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
        Comment comment = new Comment(ticket, request.getAuthor(), request.getContent(), java.time.LocalDateTime.now());
        commentRepository.save(comment);
    }

    private CommentView mapToCommentView(Comment comment){
        return new CommentView(
                comment.getAuthor(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
