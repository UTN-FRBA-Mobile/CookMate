package com.utn.cookmate.core.entity;

import java.io.Serializable;

public class Recipe implements Serializable {
    String nombre;
    Step[] pasos;

    public Recipe(String nombre, Step[] pasos) {
        this.nombre = nombre;
        this.pasos = pasos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String name) {
        this.nombre = name;
    }

    public Step[] getPasos() {
        return pasos;
    }

    public void setPasos(Step[] pasos) {
        this.pasos = pasos;
    }
}
