package com.example.pizzeria.models;

import com.example.pizzeria.enums.PizzaSize;
import com.example.pizzeria.enums.CookingType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre",nullable = false, length = 32)
    private String name;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_coccion", nullable = false)
    private CookingType cookingType;

    private String description;

    @Column(name = "precio", nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "tamano", nullable = false)
    private PizzaSize size;

    @OneToMany(mappedBy = "pizza")
    @JsonIgnore
    private List<PizzaOrder> pizzaOrders;
}
