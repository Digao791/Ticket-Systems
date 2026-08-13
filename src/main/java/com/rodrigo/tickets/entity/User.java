package com.rodrigo.tickets.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "name",
        nullable = false,
        length = 100
    )
    private String name;

    @Column(
        name = "email",
        nullable = false,
        unique = true,
        length = 150
    )
    private String email;

    @Column(
        name = "password",
        nullable = false,
        length = 30
    )
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "role",
        nullable = false,
        length = 30
    )
    private UserRole role;   

    @Column(
        name = "enabled",
        nullable = false
    )
    private boolean enabled;

    @Column(
        name = "created_at",
        nullable = false
    )
    private LocalDateTime createdAt;

    protected User(){

    }

    public User(
        String name, String email, String password, UserRole role
    ){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;

        this.enabled = true;
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}

