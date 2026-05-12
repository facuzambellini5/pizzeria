package com.example.pizzeria.dtos;

import com.example.pizzeria.enums.PizzaSize;
import com.example.pizzeria.enums.CookingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PizzaDto(

        @NotBlank(message = "El nombre de la pizza no puede estar vacío")
        String name,
        @NotBlank(message = "El tipo de cocción de la pizza no puede estar vacío")
        CookingType cookingType,
        @NotBlank(message = "La descripción de la pizza no puede estar vacía")
        String description,
        @NotBlank(message = "El tamaño de la pizza no puede estar vacío")
        PizzaSize size,
        @Positive(message = "El precio de la pizza debe ser un número positivo")
        double price
) {
}
