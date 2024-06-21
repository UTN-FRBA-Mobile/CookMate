package model.entity;


import model.ArchivoJson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class User implements Serializable {

    String nombre;
    String email;
    String contraseña;
    String[] nombreRecetas;

    public User(String nombre, String email, String contraseña, String[] nombreRecetas) {
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.nombreRecetas = nombreRecetas;
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

    public String[] getNombreRecetas() {
        return nombreRecetas;
    }

    public void setNombreRecetas(String[] nombreRecetas) {
        this.nombreRecetas = nombreRecetas;
    }

    public List<Recipe> getRecetas() {
        List<Recipe> recipes = new ArrayList<>();
        List<Recipe> recipesAll = ArchivoJson.cargarRecetas();
        Arrays.stream(this.getNombreRecetas()).forEach( nombreReceta -> {
            recipes.add(recipesAll.stream().filter(recipe -> recipe.getNombre().equals(nombreReceta)).findFirst().get());
        });
        return recipes;
    }
}

