package com.example.pizzeria.models;

import com.example.pizzeria.enums.PizzaSize;
import com.example.pizzeria.enums.CookingType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private CookingType cookingType;

    private String description;

    private double price;

    @Enumerated(EnumType.STRING)
    private PizzaSize size;

    @OneToMany(mappedBy = "pizza")
    @JsonIgnore
    private List<PizzaOrder> pizzaOrders;
}
