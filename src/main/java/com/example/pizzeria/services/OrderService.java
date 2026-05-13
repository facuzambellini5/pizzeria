package com.example.pizzeria.services;

import com.example.pizzeria.dtos.OrderDto;
import com.example.pizzeria.dtos.OrderItemDto;
import com.example.pizzeria.dtos.OrderResponseDto;
import com.example.pizzeria.enums.OrderStatus;
import com.example.pizzeria.exceptions.BusinessRuleException;
import com.example.pizzeria.exceptions.ResourceNotFoundException;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.Pizza;
import com.example.pizzeria.models.PizzaOrder;
import com.example.pizzeria.repositories.IOrderRepository;
import com.example.pizzeria.repositories.IPizzaRepository;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private IOrderRepository orderRepo;
    @Autowired
    private IPizzaRepository pizzaRepo;
    @Autowired
    private InvoiceService invoiceService;

    @PreAuthorize("hasRole('MANAGER')")
    public OrderResponseDto saveOrder(OrderDto orderDto){

        Order order = new Order();

        for (OrderItemDto itemDto : orderDto.items()){
            Pizza pizza = pizzaRepo.findById(itemDto.pizzaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pizza not found with id: " + itemDto.pizzaId()));

            if (!pizza.isActive()) {
                throw new BusinessRuleException("Pizza is not active.");
            }

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

    @PreAuthorize("hasRole('MANAGER')")
    public OrderResponseDto updateOrder(Long id, OrderDto orderDto){

        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        order.setClientName(orderDto.clientName());
        order.setTimeEstimated(orderDto.timeEstimated());

        //Eliminar items anteriores
        order.getItems().clear();

        for (OrderItemDto itemDto : orderDto.items()){
            Pizza pizza = pizzaRepo.findById(itemDto.pizzaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pizza not found with id: " + itemDto.pizzaId()));

            if (!pizza.isActive()) {
                throw new BusinessRuleException("Pizza is not active.");
            }

            PizzaOrder pizzaOrder = new PizzaOrder();
            pizzaOrder.setPizza(pizza);
            pizzaOrder.setOrder(order);
            pizzaOrder.setUnitPrice(pizza.getPrice());
            pizzaOrder.setAmount(itemDto.quantity());

            order.getItems().add(pizzaOrder);
        }
        return new OrderResponseDto(orderRepo.save(order));
    }

    @PreAuthorize("hasRole('MANAGER')")
    public void deleteOrder(Long id){
        orderRepo.deleteById(id);
    }

    public List<OrderResponseDto> getAllOrders(){
        return orderRepo.findAll().stream().map(OrderResponseDto::new).toList();
    }

    //Método marcar como listo
    @PreAuthorize("hasRole('COOKER')")
    public void ready(long id_pedido){
        Order order = orderRepo.findById(id_pedido)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id_pedido));


        if (order.getStatus().equals(OrderStatus.PENDING)){
            order.setStatus(OrderStatus.READY);
        } else throw new BusinessRuleException("Order is not pending, cannot be marked as ready");

        orderRepo.save(order);
    }
    
    //Método marcar como facturado
    @PreAuthorize("hasRole('MANAGER')")
    public void invoiced(long id_pedido){
        Order order = orderRepo.findById(id_pedido)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id_pedido));

        invoiceService.createInvoice(id_pedido);

        if (order.getStatus().equals(OrderStatus.READY)) {
            order.setStatus(OrderStatus.INVOICED);
        } else throw new BusinessRuleException("Order is not ready, cannot be marked as invoiced");

        order.setDeliveredAt(LocalTime.now());
        orderRepo.save(order);
    }
}
