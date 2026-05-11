package com.example.pizzeria.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pizza_pedido")
public class PizzaOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;
    private double unitPrice;

    @ManyToOne
    private Pizza pizza;

    //relacion con order

    @ManyToOne
    private Order order;



}
