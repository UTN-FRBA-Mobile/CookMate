package application;

import model.ClientHandler;

import java.io.*;
import java.net.Socket;

public class ServerSocket {
    private static final int PUERTO = 9090;

    public static void main(String[] args) {
        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(PUERTO)) {
            System.out.println("Servidor Socket iniciado en el puerto " + PUERTO);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + socket.getInetAddress() + " o " + socket.getRemoteSocketAddress());

                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
