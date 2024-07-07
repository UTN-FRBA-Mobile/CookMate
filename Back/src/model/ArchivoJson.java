package model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import model.entity.Recipe;
import model.entity.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import model.entity.Ingredient;
import model.entity.Step;

public class ArchivoJson {
    private static final String RUTA_USUARIOS = "usuarios.json";
    private static final String RUTA_RECETAS = "recetas.json";

    public static void guardarUsuarios(List<User> usuarios) {
        try (FileWriter fileWriter = new FileWriter(RUTA_USUARIOS)) {
            Gson gson = new Gson();
            gson.toJson(usuarios, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String,User> cargarUsuarios() {
        try {
            FileReader fileReader = new FileReader(RUTA_USUARIOS);
            Gson gson = new Gson();
            Type tipoObjetoConUsuarios = new TypeToken<JsonArray>() {}.getType();
            Type tipoListaDeUsuarios = new TypeToken<ArrayList<User>>() {}.getType();
            JsonArray objetoUser = gson.fromJson(fileReader, tipoObjetoConUsuarios);
            final ArrayList<User> listaUsuarios = gson.fromJson(objetoUser, tipoListaDeUsuarios);
            
            final Map<String,User> map = new HashMap<>();
            for(final User user : listaUsuarios){
                map.put(user.getEmail(), user);
            }
            return map;
            
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static void guardarRecetas(List<Recipe> recetas) {
        try (FileWriter fileWriter = new FileWriter(RUTA_RECETAS)) {
            Gson gson = new Gson();
            gson.toJson(recetas, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Recipe> cargarLasRecetasQueSeLlamen(final List<String> nombresRecetas) {
//        final List<Recipe> lista = new ArrayList<>();
        final List<Recipe> recetas = cargarTodasLasRecetas();
//        for(final Recipe receta : recetas){
        return recetas.stream().filter(receta -> nombresRecetas.contains(receta.getNombre())).collect(Collectors.toList());
//            if(nombresRecetas.contains(receta.getNombre())){
//                lista.add(receta);
//            }//hdp usa un filterrrrr => XD
//        }
//        return lista;
    }

    public static List<Recipe> cargarLasRecetasQuePuedanHacerseCon(final List<String> ingredientesPermitidos) {
        List<Recipe> lista = new ArrayList<>();
        List<Recipe> recetas = cargarTodasLasRecetas(); // Obtener todas las recetas disponibles

        for (Recipe receta : recetas) {

            List<Ingredient> ingredienteReceta = new LinkedList<>();
            for (Step paso : receta.getPasos()) {
                // Si el paso no tiene ingredientes, lo consideramos válido automáticamente
                if (paso.getIngredientes() != null){
                    ingredienteReceta.addAll((Arrays.stream(paso.getIngredientes()).collect(Collectors.toList())));
                }

            }
            if (ingredientesPermitidos.containsAll(ingredienteReceta.stream().map(ingredient -> ingredient.getNombre().toLowerCase()).collect(Collectors.toList()))) {
                lista.add(receta);
            }
        }

        return lista;
    }

    public static List<Recipe> cargarLasRecetasQuePuedanHacerseConNoEstricto(final List<String> ingredientesPermitidos) {
        List<Recipe> lista = new ArrayList<>();
        List<Recipe> recetas = cargarTodasLasRecetas(); // Obtener todas las recetas disponibles

        for (Recipe receta : recetas) {

            List<Ingredient> ingredienteReceta = new LinkedList<>();
            for (Step paso : receta.getPasos()) {
                // Si el paso no tiene ingredientes, lo consideramos válido automáticamente
                if (paso.getIngredientes() != null){
                    ingredienteReceta.addAll((Arrays.stream(paso.getIngredientes()).collect(Collectors.toList())));
                }

            }
            //Agregar receta a la lista si tiene alguno de los ingredientes
            for (Ingredient ingrediente : ingredienteReceta) {
                if (ingredientesPermitidos.contains(ingrediente.getNombre().toLowerCase())) {
                    lista.add(receta);
                    break; // Salir del bucle si se encuentra al menos un ingrediente permitido
                }
            }
        }

        return lista;
    }

    public static List<Recipe> cargarTodasLasRecetas() {
        try (FileReader fileReader = new FileReader(RUTA_RECETAS)) {
            Gson gson = new Gson();
            Type tipoListaRecetas = new TypeToken<ArrayList<Recipe>>() {}.getType();
            return gson.fromJson(fileReader, tipoListaRecetas);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
}
