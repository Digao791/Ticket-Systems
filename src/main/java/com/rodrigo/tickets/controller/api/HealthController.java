package com.rodrigo.tickets.controller.api;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/health")
public class HealthController{

    @GetMapping
    public ResponseEntity<Map<String, Object>> health(){
        return ResponseEntity.ok(
            Map.of(
                "status", "UP",
                "application", "ticket-system",
                "timestamp", LocalDateTime.now()
            )
        );
    }
}