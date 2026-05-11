package com.example.pizzeria.dtos;

public record OrderDto(
        String clientName,
        int timeEstimated
) {
}
