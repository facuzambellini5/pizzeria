package com.example.pizzeria.dtos;

import com.example.pizzeria.enums.OrderStatus;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.PizzaOrder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class OrderResponseDto {

    private Long id;
    private String clientName;
    private LocalDate orderDate;
    private LocalTime deliveredAt;
    private int timeEstimated; // en minutos
    private OrderStatus status;
    private List<PizzaOrder> items;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.clientName = order.getClientName();
        this.orderDate = order.getOrderDate();
        this.deliveredAt = order.getDeliveredAt();
        this.timeEstimated = order.getTimeEstimated();
        this.status = order.getStatus();
        this.items = order.getItems();
    }
}
