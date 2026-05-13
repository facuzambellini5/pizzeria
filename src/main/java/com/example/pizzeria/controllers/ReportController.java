package com.example.pizzeria.controllers;

import com.example.pizzeria.repositories.IOrderRepository;
import com.example.pizzeria.repositories.IPizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reportes")
public class ReportController {

    @Autowired
    private IPizzaRepository pizzaRepo;
    @Autowired
    private IOrderRepository orderRepo;

    // pizzas más pedidas
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/pizzas-mas-pedidas")
    public ResponseEntity<?> getMostOrderedPizzas(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(pizzaRepo.findMostOrderedPizzas(start, end));
    }

    // ingresos por período
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/ingresos")
    public ResponseEntity<?> getIncome(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(orderRepo.getIncome(start, end));
    }

    // pedidos y monto por período
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/pedidos-por-periodo")
    public ResponseEntity<?> getOrderStats(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(orderRepo.getOrderStats(start, end));
    }
}