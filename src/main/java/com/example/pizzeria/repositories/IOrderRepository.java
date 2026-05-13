package com.example.pizzeria.repositories;

import com.example.pizzeria.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {

    // Cantidad de pedidos y monto total en un período de tiempo
    @Query("SELECT COUNT(DISTINCT o), COALESCE(SUM(po.amount * po.unitPrice), 0) " +
            "FROM Order o JOIN o.items po " +
            "WHERE o.orderDate BETWEEN :start AND :end")
    Object[] getOrderStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
