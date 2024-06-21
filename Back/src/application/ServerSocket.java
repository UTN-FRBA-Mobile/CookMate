package application;

import model.ClientHandler;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ServerSocket {
    private static final int PUERTO = 9090;
    private static Map<String,String> _imagenes;
    
    public static void main(String[] args) throws Exception {
        _imagenes = readImagenesFile();
        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(PUERTO)) {
            System.out.println("Servidor Socket iniciado en el puerto " + PUERTO);
            System.out.println("Hora: " + LocalDateTime.now());

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + socket.getInetAddress() + " o " + socket.getRemoteSocketAddress());
                System.out.println("Hora: " + LocalDateTime.now());

                new Thread(new ClientHandler(socket,_imagenes)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static Map readImagenesFile() throws IOException{
        final Map map = new HashMap<String,String>();
        final File imagenesFolder = new File("imagenes");
        for(final File file : imagenesFolder.listFiles()){
            final String base64 = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
            map.put(removeExtensionFromFilename(file.getName()), base64);
        }
        return map;
    }
    
    private static String removeExtensionFromFilename(final String filename){
        final int index = filename.lastIndexOf('.');
        if(index > -1){
            return filename.substring(0,index);
        }
        return filename;
    }
}
