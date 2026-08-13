package com.rodrigo.tickets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCommentRequest {

    @NotBlank(message = "Author is required")
    @Size(
        max = 100, message = "Author name must not exceed 100 characters"
    )
    private String author;

    @NotBlank(message = "Content is required")
    @Size(
        max = 5000, message = "Content must not exceed 5000 characters"
    )
    private String content;

    public CreateCommentRequest() {
        // Default constructor for deserialization
    }

    // Getters and setters
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}