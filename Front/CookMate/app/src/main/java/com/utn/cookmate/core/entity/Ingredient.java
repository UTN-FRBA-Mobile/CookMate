package com.utn.cookmate.core.entity;

import java.io.Serializable;

public class Ingredient implements Serializable {
    String nombre;
    String cantidad;
    String imagen;

    public Ingredient(String nombre, String cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
