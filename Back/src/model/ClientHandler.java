package model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import model.entity.Recipe;
import model.entity.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Map<String,String> imagenes;
    private final Map<String,User> users;

    public ClientHandler(Socket socket, final Map<String,String> imagenes, final Map<String,User> users) {
        this.socket = socket;
        this.imagenes = imagenes;
        this.users = users;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entradaObjetos = new ObjectInputStream(socket.getInputStream())
        ) {
            // Leer la solicitud del cliente
            String solicitudJson = (String) entradaObjetos.readObject();
            JsonObject solicitud = JsonParser.parseString(solicitudJson).getAsJsonObject();

            // Obtener la acción de la solicitud
            String accion = solicitud.get("action").getAsString();

            // Procesar la solicitud y enviar la respuesta al cliente
            switch (accion) {
                case "login":
                    // Lógica para loguearse
                    final String email = solicitud.get("email").getAsString();
                    final String password = solicitud.get("password").getAsString();
                    final User user = users.get(email);
                    if(user != null){
                        if(user.getContraseña().equals(password)){
                            salidaObjetos.writeObject(new Gson().toJson(ArchivoJson.cargarRecetas(user.getNombreRecetasAsList())));
                            break;
                        }
                    }
                    salidaObjetos.writeObject("{}");
                    break;
                case "searchRecipes":
                    // Lógica para obtener la lista de recetas del archivo JSON
                    final JsonArray ingredientesPermitidos =  solicitud.get("ingredientes").getAsJsonArray();
                    final List<String> ingredientesPermitidosList =  new Gson().fromJson(ingredientesPermitidos, new TypeToken<List<String>>(){}.getType());
                    final List<Recipe> recetas = ArchivoJson.cargarRecetas(ingredientesPermitidosList);
                    salidaObjetos.writeObject(new Gson().toJson(recetas));
                    break;
                case "saveRecipe":
                    // Leer la receta a guardar desde el cliente
                    Recipe receta = new Gson().fromJson(solicitud.get("receta").getAsJsonObject(), Recipe.class);

                    // Guardar la receta en el archivo JSON
                    List<Recipe> listaRecetas = ArchivoJson.cargarRecetas();
                    listaRecetas.add(receta);
                    ArchivoJson.guardarRecetas(listaRecetas);

                    // Enviar confirmación al cliente
                    salidaObjetos.writeObject("Receta guardada");
                    break;
                case "getAllUsers":
                    // Lógica para obtener los datos personales del usuario
                    List<User> usuarios = new ArrayList(ArchivoJson.cargarUsuarios().values());

                    // Enviar los datos al cliente
                    salidaObjetos.writeObject(new Gson().toJson(usuarios));
                    break;
                case "removeRecipeFromUser":
                    String emailRemove = solicitud.get("email").getAsString();
                    String nombreRecetaRemove = solicitud.get("nombreReceta").getAsString();
                    List<Recipe> recetasRemove = ArchivoJson.cargarRecetas();
                    recetasRemove.removeIf(r -> r.getNombre().equals(nombreRecetaRemove));
                    ArchivoJson.guardarRecetas(recetasRemove);
                    salidaObjetos.writeObject("Receta eliminada para el usuario: " + emailRemove);
                    break;
                case "addRecipeToUser":
                    // Lógica para agregar una receta al usuario
                    String emailAdd = solicitud.get("email").getAsString();
                    String nombreRecetaAdd = solicitud.get("nombreReceta").getAsString();
                    List<Recipe> recetasAdd = ArchivoJson.cargarRecetas();
                    Recipe recetaAdd = recetasAdd.stream().filter(r -> r.getNombre().equals(nombreRecetaAdd)).findFirst().orElse(null);
                    if (recetaAdd != null) {
                        recetasAdd.add(recetaAdd);
                        ArchivoJson.guardarRecetas(recetasAdd);
                        salidaObjetos.writeObject("Receta agregada al usuario: " + emailAdd);
                    } else {
                        salidaObjetos.writeObject("Receta no encontrada: " + nombreRecetaAdd);
                    }
                    break;
                case "downloadResources":
                    // Lógica para descargar recursos
                    final JsonArray recursos = new JsonArray();
                    for(final Map.Entry<String, String> elem : imagenes.entrySet()){
                        final JsonObject jo = new JsonObject();
                        jo.addProperty("nombre", elem.getKey());
                        jo.addProperty("base64", elem.getValue());
                        recursos.add(jo);
                    }
                    salidaObjetos.writeObject(new Gson().toJson(recursos));
                    break;
                case "getAllIngredients":
                    salidaObjetos.writeObject(new Gson().toJson(obtenerTodosLosNombresDeIngredientes()));
                    break;
                case "otraSolicitud":

                    salidaObjetos.writeObject("Respuesta a otra solicitud");
                    break;
                default:
                    // Si la solicitud no es reconocida
                    salidaObjetos.writeObject(null);
                    break;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<String> obtenerTodosLosNombresDeIngredientes() {
        final List<Recipe> recetas = ArchivoJson.cargarRecetas();
        final Set<String> todosLosIngredientes = recetas.stream()
                .flatMap(receta -> Arrays.stream(receta.getPasos()))
                .flatMap(paso -> Arrays.stream(paso.getIngredientes()))
                .map(ingrediente -> ingrediente.getNombre())
                .collect(Collectors.toSet());
        final ArrayList<String> todosLosIngredientesOrdenados = new ArrayList<>(todosLosIngredientes);
        Collections.sort(todosLosIngredientesOrdenados);
        return todosLosIngredientesOrdenados;
    }
}
