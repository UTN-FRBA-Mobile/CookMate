package model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import model.entity.Recipe;
import model.entity.User;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entradaObjetos = new ObjectInputStream(socket.getInputStream())
        ) {
            // Leer la solicitud del cliente
            String solicitud = (String) entradaObjetos.readObject();

            // Procesar la solicitud y enviar la respuesta al cliente
            switch (solicitud) {
                case "obtenerRecetas":
                    // Lógica para obtener la lista de recetas del archivo JSON
                    List<Recipe> recetas = ArchivoJson.cargarRecetas();
                    salidaObjetos.writeObject(new Gson().toJson(recetas));
                    break;
                case "guardarReceta":
                    // Leer la receta a guardar desde el cliente
                    Recipe receta = (Recipe) entradaObjetos.readObject();

                    // Guardar la receta en el archivo JSON
                    List<Recipe> listaRecetas = ArchivoJson.cargarRecetas();
                    listaRecetas.add(receta);
                    ArchivoJson.guardarRecetas(listaRecetas);

                    // Enviar confirmación al cliente
                    salidaObjetos.writeObject("Receta guardada");
                    break;
                case "obtenerDatosUsuario":
                    // Lógica para obtener los datos personales del usuario
                    List<User> usuario = ArchivoJson.cargarUsuarios();

                    // Enviar los datos al cliente
                    salidaObjetos.writeObject(usuario);
                    break;
                case "otraSolicitud":
                    // Lógica para procesar otra solicitud
                    // ...
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


}
