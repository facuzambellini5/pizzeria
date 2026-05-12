package com.example.pizzeria.dtos;

public record LoginResponseDto(
        String username,
        String role,
        String message
) {}
