package com.example.pizzeria.repositories;

import com.example.pizzeria.models.Invoice;
import com.example.pizzeria.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvoiceRepository extends JpaRepository<Invoice, Long> {

    boolean existsByOrder(Order order);
}
