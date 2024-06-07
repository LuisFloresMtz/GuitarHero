package Connection.Socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket serverSocket;
    Socket clientSocket;
    ObjectOutputStream out;
    ObjectInputStream in;

    public Server() {
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("Esperando conexiones...");
            clientSocket = serverSocket.accept();

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            // Leer el mensaje de confirmación de conexión del cliente
            String message = (String) in.readObject();
            if ("CONNECTION_OK".equals(message)) {
                System.out.println("Conexión confirmada por el cliente");

                // Enviar mensaje de regreso al cliente
                out.writeObject("Conexión establecida exitosamente");
                out.flush();
            } else {
                System.out.println("Conexión no confirmada");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Server();
    }
}
