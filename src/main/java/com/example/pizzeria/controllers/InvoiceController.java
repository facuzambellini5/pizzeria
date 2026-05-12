package com.example.pizzeria.controllers;

import com.example.pizzeria.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;


}
