package com.utn.cookmate.core.entity;

import java.io.Serializable;

public class Step implements Serializable {
    int numero;
    String descripcion;
    Ingredient[] ingredientes;
    String imagen;

    public Step(int numero, String descripcion, Ingredient[] ingredientes) {
        this.numero = numero;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Ingredient[] getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(Ingredient[] ingredientes) {
        this.ingredientes = ingredientes;
    }
}
