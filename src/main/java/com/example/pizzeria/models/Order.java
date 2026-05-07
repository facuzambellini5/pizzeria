package com.example.pizzeria.models;

import com.example.pizzeria.enums.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;

    private LocalDate orderDate;

    private LocalTime deliveredAt;

    private int timeEstimated; // en minutos

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    // CascadeType.ALL: si guardás el Order, guarda también sus items automáticamente
    // orphanRemoval: si quitás un item de la lista, lo borra de la DB
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pizza> items = new ArrayList<>();

    private double total = getTotal();

    public double getTotal(){

        double total = 0;
        for(Pizza pizza : items){
            total += pizza.getPrice();
        }

        return total;
    }
}

