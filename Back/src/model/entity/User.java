package model.entity;


import java.io.Serializable;

public class User implements Serializable {

    String nombre;
    String email;
    String contraseña;
    Recipe[] recetas;

    public User(String nombre, String email, String contraseña, Recipe[] recetas) {
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.recetas = recetas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Recipe[] getRecetas() {
        return recetas;
    }

    public void setRecetas(Recipe[] recetas) {
        this.recetas = recetas;
    }
}

