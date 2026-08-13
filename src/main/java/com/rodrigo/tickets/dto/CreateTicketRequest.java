package com.rodrigo.tickets.dto;

import com.rodrigo.tickets.entity.TicketCriticality;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTicketRequest {
    
    @NotBlank(message = "O nome do ticket é obrigatório")
    @Size(max = 150, message = "O nome do ticket não pode ter mais de 150 caracteres")
    private String name;

    @NotNull(message = "O nome do proprietário do ticket é obrigatório")
    private Long ownerId;

    @NotNull(message = "A criticidade do ticket é obrigatória")
    private TicketCriticality criticality;

    @NotBlank(message = "A descrição do ticket é obrigatória")
    @Size(max = 2000, message = "A descrição do ticket não pode ter mais de 2000 caracteres")
    private String description;

    @NotNull(message = "O projeto é obrigatório.")
    private Long projectId;

    public CreateTicketRequest() {
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long owner_id) {
        this.ownerId = owner_id;
    }

    public TicketCriticality getCriticality() {
        return criticality;
    }

    public void setCriticality(TicketCriticality criticality) {
        this.criticality = criticality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProjectId() {
    return projectId;
}

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
