package com.example.pizzeria.models;

import com.example.pizzeria.enums.PizzaSize;
import com.example.pizzeria.enums.PizzaType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private PizzaType name;

    private String description;

    private float price;

    private PizzaSize size;
}
