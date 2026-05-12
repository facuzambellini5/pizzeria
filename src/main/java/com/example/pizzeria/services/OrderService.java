package com.example.pizzeria.services;

import com.example.pizzeria.dtos.OrderDto;
import com.example.pizzeria.dtos.OrderItemDto;
import com.example.pizzeria.dtos.OrderResponseDto;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.Pizza;
import com.example.pizzeria.models.PizzaOrder;
import com.example.pizzeria.repositories.IOrderRepository;
import com.example.pizzeria.repositories.IPizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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

    //Método marcar como facturado

}
