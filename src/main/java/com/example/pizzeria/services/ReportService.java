package com.example.pizzeria.services;

import com.example.pizzeria.dtos.IncomeReportDto;
import com.example.pizzeria.dtos.OrdersReportDto;
import com.example.pizzeria.repositories.IInvoiceRepository;
import com.example.pizzeria.repositories.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportService {

    @Autowired
    private IInvoiceRepository invoiceRepo;
    @Autowired
    private IOrderRepository orderRepo;

    // ingresos por período
    public IncomeReportDto getIncome(LocalDateTime start, LocalDateTime end) {
        Object[] stats = invoiceRepo.getIncomeStats(start, end);
        return new IncomeReportDto(
                start,
                end,
                ((Number) stats[0]).doubleValue(),
                ((Number) stats[1]).longValue()
        );
    }

    // pedidos por período
    public OrdersReportDto getOrderStats(LocalDateTime start, LocalDateTime end) {
        Object[] stats = orderRepo.getOrderStats(start, end);
        return new OrdersReportDto(
                start,
                end,
                ((Number) stats[0]).longValue(),
                ((Number) stats[1]).doubleValue()
        );
    }
}