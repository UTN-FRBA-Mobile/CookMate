package model.entity;

import java.io.Serializable;

public class Recipe implements Serializable {
    String nombre;
    String imagen;
    Step[] pasos;

    public Recipe(String nombre, String imagen, Step[] pasos) {
        this.nombre = nombre;
        this.imagen = imagen;
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

    public String getImagen() {
        return imagen;
    }
}
