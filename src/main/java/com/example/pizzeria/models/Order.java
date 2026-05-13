package com.example.pizzeria.models;

import com.example.pizzeria.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "pedido")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero")
    private Long id;

    @Column(name = "nombre_cliente", nullable = false, length = 32)
    private String clientName;

    @CreationTimestamp
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "hora_entrega")
    private LocalTime deliveredAt;

    @Column(name = "tiempo_estimado")
    private int timeEstimated; // en minutos

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PizzaOrder> items = new ArrayList<>();
}

