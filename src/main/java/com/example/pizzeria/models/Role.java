package com.example.pizzeria.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRole;

    private String name;

    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();
}
