package com.rodrigo.tickets.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Comment() {
        // Default constructor for JPA
    }

    public Comment(Ticket ticket, String author, String content, LocalDateTime createdAt) {
        this.ticket = ticket;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
    }

    //getters and setters
    public Long getId() {
        return id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    
}
