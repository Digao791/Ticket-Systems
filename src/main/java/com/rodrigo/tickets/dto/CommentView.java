package com.rodrigo.tickets.dto;

import java.time.LocalDateTime;

public record CommentView(String author, String content, LocalDateTime createdAt) {
    
}
