package com.example.pizzeria.services;

import com.example.pizzeria.dtos.InvoiceResponseDto;
import com.example.pizzeria.enums.OrderStatus;
import com.example.pizzeria.exceptions.BusinessRuleException;
import com.example.pizzeria.exceptions.ResourceNotFoundException;
import com.example.pizzeria.models.Invoice;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.repositories.IInvoiceRepository;
import com.example.pizzeria.repositories.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private IInvoiceRepository invoiceRepo;
    @Autowired
    private IOrderRepository orderRepo;

    public void createInvoice(Long idOrder) {

        Order order = orderRepo.findById(idOrder)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + idOrder));

        if (order.getStatus() != OrderStatus.READY) {
            throw new BusinessRuleException("Order status is not READY. Cannot create invoice.");
        }

        if (invoiceRepo.existsByOrder(order)) {
            throw new BusinessRuleException("Invoice already exists for this order.");
        }

        Invoice invoice = new Invoice();

        invoice.setOrder(order);
        invoice.setIssuedAt(LocalDateTime.now());

        invoiceRepo.save(invoice);
    }

    public List<InvoiceResponseDto> getInvoices() {
        return invoiceRepo.findAll().stream()
                .map(InvoiceResponseDto::new)
                .toList();
    }
}
