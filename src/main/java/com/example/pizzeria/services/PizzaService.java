package com.example.pizzeria.services;

import com.example.pizzeria.dtos.PizzaDto;
import com.example.pizzeria.models.Pizza;
import com.example.pizzeria.repositories.IPizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PizzaService {

    @Autowired
    private IPizzaRepository pizzaRepo;

    public Pizza savePizza(PizzaDto pizzaDto) {

        Pizza pizza = new Pizza();

        pizza.setName(pizzaDto.name());
        pizza.setCookingType(pizzaDto.cookingType());
        pizza.setDescription(pizzaDto.description());
        pizza.setSize(pizzaDto.size());
        pizza.setPrice(pizzaDto.price());

        return pizzaRepo.save(pizza);
    }

    public Pizza editPizza(Long id, PizzaDto pizzaDto) {

        Pizza pizza = pizzaRepo.findById(id).orElseThrow(() -> new RuntimeException("Pizza not found"));

        pizza.setName(pizzaDto.name());
        pizza.setCookingType(pizzaDto.cookingType());
        pizza.setDescription(pizzaDto.description());
        pizza.setSize(pizzaDto.size());
        pizza.setPrice(pizzaDto.price());

        return pizzaRepo.save(pizza);
    }

    public void deletePizza(Long id){
        pizzaRepo.deleteById(id);
    }

    public List<Pizza> getPizzas(){
        return pizzaRepo.findAll();
    }
}
