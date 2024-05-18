package model.entity;

import application.DataInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ArchivoJson {
    private static final String RUTA_USUARIOS = "usuarios.json";
    private static final String RUTA_RECETAS = "recetas.json";

    static {
        // Inicializar los archivos si no existen
        DataInitializer.initializeDataFiles();
    }

    public static void guardarUsuarios(List<User> usuarios) {
        try (FileWriter fileWriter = new FileWriter(RUTA_USUARIOS)) {
            Gson gson = new Gson();
            gson.toJson(usuarios, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> cargarUsuarios() {
        try (FileReader fileReader = new FileReader(RUTA_USUARIOS)) {
            Gson gson = new Gson();
            Type tipoListaUsuarios = new TypeToken<ArrayList<User>>() {}.getType();
            return gson.fromJson(fileReader, tipoListaUsuarios);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
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

    public static List<Recipe> cargarRecetas() {
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
