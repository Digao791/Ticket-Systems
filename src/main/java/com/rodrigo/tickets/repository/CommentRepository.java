package com.rodrigo.tickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.tickets.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Additional query methods can be defined here if needed
    
    List<Comment> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
