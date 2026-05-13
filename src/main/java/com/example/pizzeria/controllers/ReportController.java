package com.example.pizzeria.controllers;

import com.example.pizzeria.repositories.IPizzaRepository;
import com.example.pizzeria.services.ReportService;
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
@PreAuthorize("hasRole('OWNER')")
public class ReportController {

    @Autowired
    private IPizzaRepository pizzaRepo;
    @Autowired
    private ReportService reportService;

    // pizzas más pedidas
    @GetMapping("/pizzas-mas-pedidas")
    public ResponseEntity<?> getMostOrderedPizzas(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(pizzaRepo.findMostOrderedPizzas(start, end));
    }

    // ingresos por período
    @GetMapping("/ingresos")
    public ResponseEntity<?> getIncome(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(reportService.getIncome(start, end));
    }

    // pedidos por período
    @GetMapping("/pedidos-por-periodo")
    public ResponseEntity<?> getOrderStats(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(reportService.getOrderStats(start, end));
    }
}