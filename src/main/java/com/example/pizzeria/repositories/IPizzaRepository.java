package com.example.pizzeria.repositories;

import com.example.pizzeria.models.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IPizzaRepository extends JpaRepository<Pizza, Long> {

    //TODO implementar método para buscar pizzas por nombre, tamaño y tipo

    List<Pizza> findByActiveTrue();

    //Variedades y tipos de pizzas más pedidas por los clientes.
    @Query("SELECT po.pizza.name, po.pizza.cookingType, po.pizza.size, SUM(po.amount) " +
            "FROM PizzaOrder po " +
            "WHERE po.order.orderDate BETWEEN :start AND :end " +
            "GROUP BY po.pizza.id, po.pizza.name, po.pizza.cookingType, po.pizza.size " +
            "ORDER BY SUM(po.amount) DESC")
    List<Object[]> findMostOrderedPizzas(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);


}
