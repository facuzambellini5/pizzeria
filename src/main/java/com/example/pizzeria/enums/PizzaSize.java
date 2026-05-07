package com.example.pizzeria.enums;

public enum PizzaSize {
    SMALL(8),
    MEDIUM(10),
    LARGE(12);

    private final int portions;

    PizzaSize(int portions) {
        this.portions = portions;
    }
    public int getPortions() {
        return portions;
    }
}
