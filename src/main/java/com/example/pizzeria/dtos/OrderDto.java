package com.example.pizzeria.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderDto(

        @NotBlank(message = "El nombre del cliente es obligatorio")
        String clientName,
        @NotBlank(message = "El tiempo estimado es obligatorio")
        int timeEstimated,
        @NotEmpty(message = "La orden debe contener al menos un item")
        List<OrderItemDto> items
) {
}
