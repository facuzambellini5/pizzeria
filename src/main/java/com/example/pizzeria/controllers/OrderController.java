package com.example.pizzeria.controllers;

import com.example.pizzeria.dtos.OrderDto;
import com.example.pizzeria.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedido")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/guardar")
    public ResponseEntity<?> saveOrder(@RequestBody OrderDto orderDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.saveOrder(orderDto));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editOrder(@PathVariable Long id,
                                       @RequestBody OrderDto orderDto) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(id, orderDto));

    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/traer")
    public ResponseEntity<?> getOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}

