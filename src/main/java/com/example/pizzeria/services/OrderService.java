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

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

        log.info("Registrando nuevo pedido: cliente={}, items={}",
                orderDto.clientName(), orderDto.items().size());

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

        log.info("Modificando pedido id={}", id);

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
        log.info("Eliminando pedido id={}", id);
        orderRepo.deleteById(id);
    }

    public List<OrderResponseDto> getAllOrders(){
        return orderRepo.findAll().stream().map(OrderResponseDto::new).toList();
    }

    // Marcar como listo
    @PreAuthorize("hasRole('COOKER')")
    public void setReady(long idPedido){

        log.info("Marcando pedido id={} como LISTO", idPedido);

        Order order = orderRepo.findById(idPedido)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + idPedido));


        if (order.getStatus().equals(OrderStatus.PENDING)){
            order.setStatus(OrderStatus.READY);
        } else throw new BusinessRuleException("Order is not pending, cannot be marked as ready");

        orderRepo.save(order);
    }
    
    //Método marcar como facturado
    @PreAuthorize("hasRole('MANAGER')")
    public void invoiced(Long idPedido){

        log.info("Iniciando facturación del pedido id={}", idPedido);

        Order order = orderRepo.findById(idPedido)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + idPedido));

        invoiceService.createInvoice(idPedido);

        if (order.getStatus().equals(OrderStatus.READY)) {
            order.setStatus(OrderStatus.INVOICED);
        } else throw new BusinessRuleException("Order is not ready, cannot be marked as invoiced");

        order.setDeliveredAt(LocalTime.now());
        orderRepo.save(order);
    }
}
