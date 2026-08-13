package com.rodrigo.tickets.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_activities")
public class TicketActivity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", length = 30)
    private TicketStatus oldStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", length = 30)
    private TicketStatus newStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "old_owner_id")
    private User oldOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_owner_id")
    private User newOwner;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected TicketActivity() {
        // Default constructor for JPA
    }

    public TicketActivity(Ticket ticket, String author, String comment, TicketStatus oldStatus, TicketStatus newStatus, User oldOwner, User newOwner, LocalDateTime createdAt) {
        this.ticket = ticket;
        this.author = author;
        this.comment = comment;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.oldOwner = oldOwner;
        this.newOwner = newOwner;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getAuthor() {
        return author;
    }

    public User getOldOwner(){
        return oldOwner;
    }

    public User getNewOwner(){
        return newOwner;
    }
    public String getComment() {
        return comment;
    }

    public TicketStatus getOldStatus() {
        return oldStatus;
    }

    public TicketStatus getNewStatus() {
        return newStatus;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
}
