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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        final List<Recipe> lista = new ArrayList<>();
        final List<Recipe> recetas = cargarTodasLasRecetas();
        for(final Recipe receta : recetas){
            if(nombresRecetas.contains(receta.getNombre())){
                lista.add(receta);
            }//hdp usa un filterrrrr
        }
        return lista;
    }
    
    public static List<Recipe> cargarLasRecetasQuePuedanHacerseCon(final List<String> ingredientesPermitidos) {
        final List<Recipe> lista = new ArrayList<>();
        final List<Recipe> recetas = cargarTodasLasRecetas();
        
//            lista.add(recetas.stream().filter(recipe -> paso.getIn().equals(valida)).findFirst().get());
            
            loopReceta: for(final Recipe receta : recetas){
                for(final Step paso : receta.getPasos()){
                    for(final Ingredient ingredienteNecesario : paso.getIngredientes()){
                        if(!ingredientesPermitidos.contains(ingredienteNecesario.getNombre())){
                            continue loopReceta; //necesito un ingrediente que no tengo, esta receta no sirve mas!
                        }
                    }
                }
                lista.add(receta);
            }
            
//            List<Recipe> todosLosIngredientes = recetas.stream()
//                .flatMap(receta -> Arrays.stream(receta.getPasos()))
//                .flatMap(paso -> Arrays.stream(paso.getIngredientes()))
//                .filter(ingrediente -> ingrediente.getNombre().equals(ingredienteValido))
//                .collect(receta);
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
