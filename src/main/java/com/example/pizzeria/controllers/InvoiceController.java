package com.example.pizzeria.controllers;

import com.example.pizzeria.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/factura")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/traer")
    public ResponseEntity<?> getInvoices() {
        return ResponseEntity.ok(invoiceService.getInvoices());
    }


}
