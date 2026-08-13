package com.rodrigo.tickets.dto;

import com.rodrigo.tickets.entity.TicketStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTicketActivityRequest {

    @Size(max = 2000, message = "Comment must not exceed 2000 characters")
    private String comment;

    @NotNull(message = "status cannot be null")
    private TicketStatus status;

    @NotNull(message = "Owner cannot be null")
    private Long ownerId;

    public CreateTicketActivityRequest() {
        // Default constructor for deserialization
    }   

    //getters and setters

    public String getComment(){
        return comment;
    }

    public TicketStatus getStatus(){
        return status;
    }

    public Long getOwnerId(){
        return ownerId;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public void setStatus(TicketStatus status){
        this.status = status;
    }

    public void setOwnerId(Long owner_id){
        this.ownerId = owner_id;
    }
    
}
