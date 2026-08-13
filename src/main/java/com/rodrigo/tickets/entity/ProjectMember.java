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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "project_members",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_project_member",
            columnNames = {
                "project_id",
                "user_id"
            }
        )
    }
)
public class ProjectMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "project_id",
        nullable = false
    )
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;

    @Column(
        name = "joined_at",
        nullable = false
    )
    private LocalDateTime joinedAt;

    protected ProjectMember(){

    }

    public ProjectMember(
        Project project,
        User user
    ){
        this.project = project;
        this.user = user;
        this.joinedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}
