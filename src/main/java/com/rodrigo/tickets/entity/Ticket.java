package com.rodrigo.tickets.entity;

import java.time.LocalDate;

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
@Table(name = "tickets")
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "name",
        nullable = false,
        length = 150
    )
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "owner_id",
        nullable = false
    )
    private User ownerId;

    @Column(
        name = "created_at",
        nullable = false
    )
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "criticality",
        nullable = false,
        length = 20
    )
    private TicketCriticality criticality;

    @Column(
        name = "description",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false,
        length = 30
    )
    private TicketStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "project_id",
        nullable = false
    )
    private Project project;

    protected Ticket() {
    }

    public Ticket(String name, Project project, User ownerId, String description, LocalDate createdAt, TicketCriticality criticality) {
        this.name = name;
        this.project = project;
        this.ownerId = ownerId;
        this.description = description;
        this.createdAt = createdAt;
        this.criticality = criticality;
        this.status = TicketStatus.OPEN;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return ownerId;
    }

    public void setOwner(User owner) {
        this.ownerId = owner;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public TicketCriticality getCriticality() {
        return criticality;
    }

    public void setCriticality(TicketCriticality criticality) {
        this.criticality = criticality;
    }

    public String getCriticalityCssClass() {
        return criticality.getCssClass();
    }

    public void setCriticalityCssClass(String criticalityCssClass) {
        // This method is intentionally left empty as the CSS class is derived from the criticality enum.
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
