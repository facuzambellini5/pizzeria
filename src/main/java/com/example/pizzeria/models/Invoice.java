package com.example.pizzeria.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "factura")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero")
    private Long idInvoice;

    @CreationTimestamp
    @Column(name = "emision", nullable = false)
    private LocalDateTime issuedAt;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Order order;
}
