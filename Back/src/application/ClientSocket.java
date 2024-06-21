package application;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import model.entity.Ingredient;
import model.entity.Recipe;
import model.entity.Step;
import model.entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;

public class ClientSocket {
    private static final String SERVIDOR_IP = "198.199.90.109"; // Dirección IP del servidor
    private static final int PUERTO = 9090;

    public static void main(String[] args) {
        // Llamadas de prueba a las diferentes funcionalidades
        obtenerRecetas();
        obtenerUsuarios();
        obtenerIngredientes();
        descargarRecursos();
        login("test@example.com", "password123");
    }

    private static void obtenerRecetas() {
        try (
                Socket socket = new Socket(SERVIDOR_IP, PUERTO);
                ObjectOutputStream salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entradaObjetos = new ObjectInputStream(socket.getInputStream())
        ) {
            // Crear solicitud para obtener recetas
            JsonObject solicitud = new JsonObject();
            solicitud.addProperty("action", "searchRecipes");

            // Enviar solicitud al servidor
            salidaObjetos.writeObject(solicitud.toString());

            // Leer la respuesta del servidor
            String respuestaJson = (String) entradaObjetos.readObject();
            Type listType = new TypeToken<List<Recipe>>() {}.getType();
            List<Recipe> recetas = new Gson().fromJson(respuestaJson, listType);

            // Imprimir recetas
            for (Recipe receta : recetas) {
                System.out.println("Receta: " + receta.getNombre());
                for (Step paso : receta.getPasos()) {
                    System.out.println("Paso " + paso.getNumero() + ": " + paso.getDescripcion());
                    for (Ingredient ingrediente : paso.getIngredientes()) {
                        System.out.println(" - Ingrediente: " + ingrediente.getNombre() + ", Cantidad: " + ingrediente.getCantidad());
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void obtenerUsuarios() {
        try (
                Socket socket = new Socket(SERVIDOR_IP, PUERTO);
                ObjectOutputStream salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entradaObjetos = new ObjectInputStream(socket.getInputStream())
        ) {
            // Crear solicitud para obtener usuarios
            JsonObject solicitud = new JsonObject();
            solicitud.addProperty("action", "getAllUsers");

            // Enviar solicitud al servidor
            salidaObjetos.writeObject(solicitud.toString());

            // Leer la respuesta del servidor
            String respuestaJson = (String) entradaObjetos.readObject();
            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> usuarios = new Gson().fromJson(respuestaJson, listType);

            // Imprimir usuarios
            for (User usuario : usuarios) {
                System.out.println("Usuario: " + usuario.getNombre());
                for (Recipe receta : usuario.getRecetas()) {
                    System.out.println("Receta: " + receta.getNombre());
                    for (Step paso : receta.getPasos()) {
                        System.out.println("Paso " + paso.getNumero() + ": " + paso.getDescripcion());
                        for (Ingredient ingrediente : paso.getIngredientes()) {
                            System.out.println(" - Ingrediente: " + ingrediente.getNombre() + ", Cantidad: " + ingrediente.getCantidad());
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void obtenerIngredientes() {
        try (
                Socket socket = new Socket(SERVIDOR_IP, PUERTO);
                ObjectOutputStream salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entradaObjetos = new ObjectInputStream(socket.getInputStream())
        ) {
            // Crear solicitud para obtener ingredientes
            JsonObject solicitud = new JsonObject();
            solicitud.addProperty("action", "getAllIngredients");

            // Enviar solicitud al servidor
            salidaObjetos.writeObject(solicitud.toString());

            // Leer la respuesta del servidor
            String respuestaJson = (String) entradaObjetos.readObject();
            Type listType = new TypeToken<List<Ingredient>>() {}.getType();
            List<Ingredient> ingredientes = new Gson().fromJson(respuestaJson, listType);

            // Imprimir ingredientes
            for (Ingredient ingrediente : ingredientes) {
                System.out.println("Ingrediente: " + ingrediente.getNombre() + ", Cantidad: " + ingrediente.getCantidad());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void descargarRecursos() {
        try (
                Socket socket = new Socket(SERVIDOR_IP, PUERTO);
                ObjectOutputStream salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entradaObjetos = new ObjectInputStream(socket.getInputStream())
        ) {
            // Crear solicitud para descargar recursos
            JsonObject solicitud = new JsonObject();
            solicitud.addProperty("action", "downloadResources");

            // Enviar solicitud al servidor
            salidaObjetos.writeObject(solicitud.toString());

            // Leer la respuesta del servidor
            String respuestaJson = (String) entradaObjetos.readObject();
            JsonArray recursos = new Gson().fromJson(respuestaJson, JsonArray.class);

            // Imprimir recursos
            for (int i = 0; i < recursos.size(); i++) {
                JsonObject recurso = recursos.get(i).getAsJsonObject();
                System.out.println("Recurso: " + recurso.get("nombre").getAsString());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void login(String email, String password) {
        try (
                Socket socket = new Socket(SERVIDOR_IP, PUERTO);
                ObjectOutputStream salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entradaObjetos = new ObjectInputStream(socket.getInputStream())
        ) {
            // Crear solicitud de login
            JsonObject solicitud = new JsonObject();
            solicitud.addProperty("action", "login");
            solicitud.addProperty("email", email);
            solicitud.addProperty("password", password);

            // Enviar solicitud al servidor
            salidaObjetos.writeObject(solicitud.toString());

            // Leer la respuesta del servidor
            String respuestaJson = (String) entradaObjetos.readObject();
            Type listType = new TypeToken<List<Recipe>>() {}.getType();
            List<Recipe> recetas = new Gson().fromJson(respuestaJson, listType);

            // Imprimir recetas (respuesta de login)
            for (Recipe receta : recetas) {
                System.out.println("Receta: " + receta.getNombre());
                for (Step paso : receta.getPasos()) {
                    System.out.println("Paso " + paso.getNumero() + ": " + paso.getDescripcion());
                    for (Ingredient ingrediente : paso.getIngredientes()) {
                        System.out.println(" - Ingrediente: " + ingrediente.getNombre() + ", Cantidad: " + ingrediente.getCantidad());
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
