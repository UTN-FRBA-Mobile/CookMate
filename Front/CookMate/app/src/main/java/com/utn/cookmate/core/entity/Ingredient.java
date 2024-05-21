package com.utn.cookmate.core.entity;

import java.io.Serializable;

public class Ingredient implements Serializable {
    String nombre;
    int cantidad;

    public Ingredient(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
