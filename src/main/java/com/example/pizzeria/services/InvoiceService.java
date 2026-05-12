package com.example.pizzeria.services;

import com.example.pizzeria.repositories.IInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    private IInvoiceRepository invoiceRepo;



}
