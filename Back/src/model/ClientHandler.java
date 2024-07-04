package model;

import application.DataInitializer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import model.entity.Recipe;
import model.entity.User;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Map<String,String> imagenes;

    public ClientHandler(Socket socket, final Map<String,String> imagenes) {
        this.socket = socket;
        this.imagenes = imagenes;
        DataInitializer.initializeDataFiles();
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
            System.out.println("Solicitud del cliente: " + solicitud);

            // Obtener la acción de la solicitud
            String accion = solicitud.get("action").getAsString();
            System.out.println("Accion a realizar: " + accion);

            // Procesar la solicitud y enviar la respuesta al cliente
            switch (accion) {
                case "login":
                    // Lógica para loguearse
                    final String email = solicitud.get("email").getAsString();
                    final String password = solicitud.get("password").getAsString();
                    final User user = ArchivoJson.cargarUsuarios().get(email);
                    if(user != null){
                        if(user.getContraseña().equals(password)){
                            salidaObjetos.writeObject(new Gson().toJson(ArchivoJson.cargarLasRecetasQueSeLlamen(user.getNombreRecetasAsList())));
                            break;
                        }
                    }
                    salidaObjetos.writeObject("{}");
                    break;
                case "register":
                    boolean registerOk = false;
                    // Lógica para registrarse
                    try{
                        final String registerEmail = solicitud.get("email").getAsString();
                        final String registerPassword = solicitud.get("password").getAsString();
                        final String registerNombre = solicitud.get("name").getAsString();
                        final Map<String, User> usuarios = ArchivoJson.cargarUsuarios();
                        if(usuarios.containsKey(registerEmail)){
                            salidaObjetos.writeObject(registerOk);
                            throw new RuntimeException("El usuario ya se encuentra registrado");
                        }
                        final User registerUser = new User(registerNombre, registerEmail, registerPassword, new String[]{});
                        usuarios.put(registerUser.getEmail(),registerUser);
                        ArchivoJson.guardarUsuarios(new ArrayList(usuarios.values()));
                        registerOk = Boolean.TRUE;
                    }catch (Exception e){
                        System.out.println("Error al registrarse");
                        e.printStackTrace();
                    }
                    salidaObjetos.writeObject(registerOk);
                    System.out.println("respuesta: " + registerOk);
                    break;

                case "searchRecipes":
                    // Lógica para obtener la lista de recetas del archivo JSON
                    final JsonArray ingredientesPermitidos =  solicitud.get("ingredientes").getAsJsonArray();
                    final List<String> ingredientesPermitidosList =  new Gson().fromJson(ingredientesPermitidos, new TypeToken<List<String>>(){}.getType());
                    final List<Recipe> recetas = ArchivoJson.cargarLasRecetasQuePuedanHacerseCon(ingredientesPermitidosList.stream().map(String::toLowerCase).collect(Collectors.toList()));
                    salidaObjetos.writeObject(new Gson().toJson(recetas));
                    System.out.println("respuesta: " + new Gson().toJson(recetas));
                    break;
                case "searchRecipesNonStrict":
                    // Lógica para obtener la lista de recetas del archivo JSON
                    final JsonArray ingredientesPermitidos2 =  solicitud.get("ingredientes").getAsJsonArray();
                    final List<String> ingredientesPermitidosList2 =  new Gson().fromJson(ingredientesPermitidos2, new TypeToken<List<String>>(){}.getType());
                    final List<Recipe> recetas2 = ArchivoJson.cargarLasRecetasQuePuedanHacerseConNoEstricto(ingredientesPermitidosList2.stream().map(String::toLowerCase).collect(Collectors.toList()));
                    salidaObjetos.writeObject(new Gson().toJson(recetas2));
                    System.out.println("respuesta: " + new Gson().toJson(recetas2));
                    break;
                case "removeRecipeFromUser":
                    String emailRemove = solicitud.get("email").getAsString();
                    String nombreRecetaRemove = solicitud.get("nombreReceta").getAsString();

                    final Map<String, User> usuariosHoy = ArchivoJson.cargarUsuarios();
                    final User usuarioACambiar = usuariosHoy.remove(emailRemove);
                    if(usuarioACambiar != null){
                        final List<String> recetasAConservar = usuarioACambiar.getNombreRecetasAsList();
                        recetasAConservar.remove(nombreRecetaRemove);
                        usuarioACambiar.setNombreRecetas(recetasAConservar.toArray(new String[]{}));
                        usuariosHoy.put(usuarioACambiar.getEmail(),usuarioACambiar);
                        ArchivoJson.guardarUsuarios(new ArrayList(usuariosHoy.values()));
//                        salidaObjetos.writeObject(new Gson().toJson(ArchivoJson.cargarRecetas(usuario.getNombreRecetasAsList())));
                        salidaObjetos.writeObject("Receta eliminada para el usuario: " + emailRemove);
                    }
                    break;
                case "addRecipeToUser":
                    // Lógica para agregar una receta al usuario
                    String emailAdd = solicitud.get("email").getAsString();
                    String nombreRecetaAdd = solicitud.get("nombreReceta").getAsString();
                    
                    final Map<String, User> usuariosActuales = ArchivoJson.cargarUsuarios();
                    final User usuarioAModificar = usuariosActuales.remove(emailAdd);
                    if(usuarioAModificar != null){
                        final List<String> recetasDelUsuario = usuarioAModificar.getNombreRecetasAsList();
                        recetasDelUsuario.add(nombreRecetaAdd);
                        usuarioAModificar.setNombreRecetas(recetasDelUsuario.toArray(new String[]{}));
                        usuariosActuales.put(usuarioAModificar.getEmail(),usuarioAModificar);
                        ArchivoJson.guardarUsuarios(new ArrayList(usuariosActuales.values()));
//                        salidaObjetos.writeObject(new Gson().toJson(ArchivoJson.cargarRecetas(usuario.getNombreRecetasAsList())));
                        salidaObjetos.writeObject("Receta agregada al usuario: " + emailAdd);
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
        final List<Recipe> recetas = ArchivoJson.cargarTodasLasRecetas();
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
