package com.example.pizzeria.repositories;

import com.example.pizzeria.models.Invoice;
import com.example.pizzeria.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface IInvoiceRepository extends JpaRepository<Invoice, Long> {
    boolean existsByOrder(Order order);
}
