package com.example.pizzeria.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    //TODO ver qué atributos guardar
    private String firstname;
    private String lastname;
    private String password;

    @ManyToOne
    private Role role;
}
