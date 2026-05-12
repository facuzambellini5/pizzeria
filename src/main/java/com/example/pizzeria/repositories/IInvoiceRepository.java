package com.example.pizzeria.repositories;

import com.example.pizzeria.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvoiceRepository extends JpaRepository<Invoice, Long> {

    boolean existsByIdInvoice(Long idInvoice);
}
