package com.example.pizzeria.dtos;

import java.time.LocalDateTime;

public record OrdersReportDto(
        LocalDateTime desde,
        LocalDateTime hasta,
        long cantidadPedidos,
        double montoTotal
) {}
