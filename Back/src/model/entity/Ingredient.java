package model.entity;

import java.io.Serializable;

public class Ingredient implements Serializable {
    String nombre;
    int cantidad;
    String imagen;

    public Ingredient(String nombre, int cantidad, String imagen) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.imagen = imagen;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
