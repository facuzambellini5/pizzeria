package com.example.pizzeria.dtos;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemDto(
        @NotNull(message = "El id de la pizza es obligatorio")
        Long pizzaId,

        @Min(value = 1, message = "La cantidad mínima es 1")
        int quantity
) { }
