package com.example.pizzeria.dtos;

import java.time.LocalDateTime;

public record IncomeReportDto(
        LocalDateTime desde,
        LocalDateTime hasta,
        double totalRecaudado,
        long cantidadFacturas
) {}
