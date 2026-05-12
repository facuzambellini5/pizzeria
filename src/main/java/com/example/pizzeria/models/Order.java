package com.example.pizzeria.models;

import com.example.pizzeria.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "pedido")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;

    @CreationTimestamp
    private LocalDate orderDate;

    //TODO decirle a fogar que agregue atributos
    private LocalTime deliveredAt;
    private int timeEstimated; // en minutos

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    //TODO ver cascade si es necesario
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PizzaOrder> items = new ArrayList<>();


    //TODO ver si decirle a fogar que agregue atributo en bd
//    private double total = getTotal();
//
//    public double getTotal(){
//
//        double total = 0;
//        for(Pizza pizza : items){
//            total += pizza.getPrice();
//        }
//
//        return total;
//    }
}

