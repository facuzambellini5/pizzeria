package com.example.pizzeria.dtos;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {}
