package application;

import model.entity.Ingredient;
import model.entity.Recipe;
import model.entity.Step;
import model.entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientSocket {
    private static final String SERVIDOR_IP = "localhost"; // Dirección IP del servidor
    private static final int PUERTO = 12345;

    public static void main(String[] args) {
        obtenerRecetas();
        obtenerUsuarios();
    }

    private static void obtenerRecetas() {
        try (
                Socket socket = new Socket(SERVIDOR_IP, PUERTO);
                ObjectOutputStream salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entradaObjetos = new ObjectInputStream(socket.getInputStream())
        ) {
            // Solicitar obtener recetas
            salidaObjetos.writeObject("obtenerRecetas");

            // Leer la respuesta del servidor
            List<Recipe> recetas = (List<Recipe>) entradaObjetos.readObject();
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
            // Solicitar obtener usuarios
            salidaObjetos.writeObject("obtenerDatosUsuario");

            // Leer la respuesta del servidor
            List<User> usuarios = (List<User>) entradaObjetos.readObject();
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
}
