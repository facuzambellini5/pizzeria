package com.example.pizzeria.repositories;

import com.example.pizzeria.dtos.StatsDto;
import com.example.pizzeria.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {

    // Cantidad de pedidos y monto total en un período de tiempo
    @Query("SELECT new com.example.pizzeria.dtos.StatsDto("+
    "SUM(po.amount * po.unitPrice)," +
    "COUNT(DISTINCT i)" +
    ")   " +
    "FROM Invoice i JOIN i.order o JOIN o.items po " +
    "WHERE i.issuedAt BETWEEN :start AND :end")
    StatsDto getOrderStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT COALESCE(SUM(po.amount * po.unitPrice), 0) " +
            "FROM Invoice i JOIN i.order o JOIN o.items po " +
            "WHERE i.issuedAt BETWEEN :start AND :end")
    double getIncome(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


}
