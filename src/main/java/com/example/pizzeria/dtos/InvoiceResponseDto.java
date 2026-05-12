package com.example.pizzeria.dtos;
import com.example.pizzeria.models.Invoice;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceResponseDto {

    private Long idInvoice;
    private LocalDateTime issuedAt;
    private OrderResponseDto order;
    private double total;

    public InvoiceResponseDto(Invoice invoice) {
        this.idInvoice = invoice.getIdInvoice();
        this.issuedAt = invoice.getIssuedAt();
        this.order = new OrderResponseDto(invoice.getOrder());
        this.total = invoice.getOrder().getItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getAmount())
                .sum();
    }
}
