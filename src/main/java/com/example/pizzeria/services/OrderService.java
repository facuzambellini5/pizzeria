package com.example.pizzeria.services;

import com.example.pizzeria.dtos.OrderDto;
import com.example.pizzeria.dtos.OrderItemDto;
import com.example.pizzeria.dtos.OrderResponseDto;
import com.example.pizzeria.enums.OrderStatus;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.Pizza;
import com.example.pizzeria.models.PizzaOrder;
import com.example.pizzeria.repositories.IOrderRepository;
import com.example.pizzeria.repositories.IPizzaRepository;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Service
public class OrderService {

    @Autowired
    private IOrderRepository orderRepo;
    @Autowired
    private IPizzaRepository pizzaRepo;

    public OrderResponseDto saveOrder(OrderDto orderDto){

        Order order = new Order();

        for (OrderItemDto itemDto : orderDto.items()){
            Pizza pizza = pizzaRepo.findById(itemDto.pizzaId())
                    .orElseThrow(() -> new RuntimeException("Pizza not found with id: " + itemDto.pizzaId()));

            PizzaOrder pizzaOrder = new PizzaOrder();
            pizzaOrder.setPizza(pizza);
            pizzaOrder.setOrder(order);
            pizzaOrder.setUnitPrice(pizza.getPrice());
            pizzaOrder.setAmount(itemDto.quantity());

            order.getItems().add(pizzaOrder);
        }

        order.setClientName(orderDto.clientName());
        order.setTimeEstimated(orderDto.timeEstimated());

        return new OrderResponseDto(orderRepo.save(order));
    }

    public OrderResponseDto updateOrder(Long id, OrderDto orderDto){

        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setClientName(orderDto.clientName());
        order.setTimeEstimated(orderDto.timeEstimated());

        //Eliminar items anteriores
        order.getItems().clear();

        for (OrderItemDto itemDto : orderDto.items()){
            Pizza pizza = pizzaRepo.findById(itemDto.pizzaId())
                    .orElseThrow(() -> new RuntimeException("Pizza not found with id: " + itemDto.pizzaId()));

            PizzaOrder pizzaOrder = new PizzaOrder();
            pizzaOrder.setPizza(pizza);
            pizzaOrder.setOrder(order);
            pizzaOrder.setUnitPrice(pizza.getPrice());
            pizzaOrder.setAmount(itemDto.quantity());

            order.getItems().add(pizzaOrder);
        }
        return new OrderResponseDto(orderRepo.save(order));
    }

    public void deleteOrder(Long id){
        orderRepo.deleteById(id);
    }

    public List<OrderResponseDto> getAllOrders(){
        return orderRepo.findAll().stream().map(OrderResponseDto::new).toList();
    }

    //Método marcar como listo
    public void ready(long id_pedido){
        Order order = orderRepo.findById(id_pedido)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id_pedido));
        order.setStatus(OrderStatus.READY);
        orderRepo.save(order);
    }
    
    //Método marcar como facturado
    public void invoiced(long id_pedido){
        Order order = orderRepo.findById(id_pedido)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id_pedido));
        order.setStatus(OrderStatus.INVOICED);
        order.setDeliveredAt(LocalTime.now());
        orderRepo.save(order);
    }
}
