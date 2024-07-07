package model.entity;

import java.io.Serializable;

public class Step implements Serializable {
    int numero;
    String descripcion;
    String imagen;
    Ingredient[] ingredientes;
    Integer duracion;

    public Step(int numero, String descripcion, String imagen, Ingredient[] ingredientes) {
        this(numero,descripcion,imagen,ingredientes,null);
    }
    
    public Step(int numero, String descripcion, String imagen, Ingredient[] ingredientes, Integer duracion) {
        this.numero = numero;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.ingredientes = ingredientes;
        this.duracion = duracion;
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

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
}
