package com.example.pizzeria.services;

import com.example.pizzeria.dtos.PizzaDto;
import com.example.pizzeria.exceptions.ResourceNotFoundException;
import com.example.pizzeria.models.Pizza;
import com.example.pizzeria.repositories.IPizzaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PizzaService {

    @Autowired
    private IPizzaRepository pizzaRepo;

    @PreAuthorize("hasRole('OWNER')")
    public Pizza savePizza(PizzaDto pizzaDto) {

        log.info("Registrando nueva pizza: nombre={}, tipo={}, tamaño={}",
                pizzaDto.name(), pizzaDto.cookingType(), pizzaDto.size());

        Pizza pizza = new Pizza();

        pizza.setName(pizzaDto.name());
        pizza.setCookingType(pizzaDto.cookingType());
        pizza.setDescription(pizzaDto.description());
        pizza.setSize(pizzaDto.size());
        pizza.setPrice(pizzaDto.price());

        return pizzaRepo.save(pizza);
    }

    @PreAuthorize("hasRole('OWNER')")
    public Pizza editPizza(Long id, PizzaDto pizzaDto) {

        log.info("Modificando pizza id={}", id);

        Pizza pizza = pizzaRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pizza not found"));

        pizza.setName(pizzaDto.name());
        pizza.setCookingType(pizzaDto.cookingType());
        pizza.setDescription(pizzaDto.description());
        pizza.setSize(pizzaDto.size());
        pizza.setPrice(pizzaDto.price());

        return pizzaRepo.save(pizza);
    }

    //soft delete
    @PreAuthorize("hasRole('OWNER')")
    public void deletePizza(Long id){

        log.info("Eliminando (soft delete) pizza id={}", id);

        Pizza pizza = pizzaRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pizza not found"));
        pizza.setActive(false);
        pizzaRepo.save(pizza);
    }

    public List<Pizza> getPizzas(){
        log.debug("Consulta de menú: pizzas activas encontradas");
        return pizzaRepo.findByActiveTrue();
    }
}
