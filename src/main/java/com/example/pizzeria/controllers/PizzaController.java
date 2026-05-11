package com.example.pizzeria.controllers;

import com.example.pizzeria.dtos.PizzaDto;

import com.example.pizzeria.services.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pizza")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    @PostMapping("/guardar")
    public ResponseEntity<?> savePizza(@RequestBody PizzaDto pizzaDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pizzaService.savePizza(pizzaDto));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editPizza(@PathVariable Long id,
                                       @RequestBody PizzaDto pizzaDto){

        return ResponseEntity.status(HttpStatus.OK).body(pizzaService.editPizza(id, pizzaDto));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deletePizza(@PathVariable Long id){
        pizzaService.deletePizza(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/traer")
    public ResponseEntity<?> getPizzas(){
        return ResponseEntity.ok(pizzaService.getPizzas());
    }
}
