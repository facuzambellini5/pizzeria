package com.example.pizzeria.repositories;

import com.example.pizzeria.models.Invoice;
import com.example.pizzeria.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface IInvoiceRepository extends JpaRepository<Invoice, Long> {

    boolean existsByOrder(Order order);

    // ingresos totales en un período de tiempo
    @Query("SELECT COALESCE(SUM(po.amount * po.unitPrice), 0), COUNT(DISTINCT i) " + //COALESCE hace que devuelva 0 si no hay facturas
            "FROM Invoice i JOIN i.order o JOIN o.items po " +
            "WHERE i.issuedAt BETWEEN :start AND :end")
    Object[] getIncomeStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
