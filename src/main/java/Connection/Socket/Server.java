package Connection.Socket;

import Components.Menu.GameMenu;
import com.studiohartman.jamepad.ControllerManager;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.IOException;

public class Server {
    ServerSocket serverSocket;
    Socket clientSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    JFrame frame;
    private final ControllerManager controllers;
    GameMenu gameMenu;

    public Server(JFrame frame, GameMenu gameMenu) {
        this.frame = frame;
        controllers = new ControllerManager();
        this.gameMenu = gameMenu;
        handleConnection(frame);
    }

    private void handleConnection(JFrame frame) {
        DatagramSocket s = null;
        try {
            s = new DatagramSocket(5000);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Servidor UDP activo. Esperando conexiones...");

        DatagramSocket finalS = s;
        new Thread(() -> {
            try {
                while (true) {
                    DatagramPacket recibido = new DatagramPacket(new byte[1024], 1024);
                    System.out.println("Esperando mensaje...");
                    finalS.receive(recibido);
                    System.out.println("Ha llegado una petición");
                    System.out.println("Procedente de: " + recibido.getAddress());
                    System.out.println("En el puerto: " + recibido.getPort());

                    String dato = new String(recibido.getData()).split("\0")[0];
                    System.out.println("Dato recibido: " + dato);

                    String message = "HORA DEL SERVIDOR " + new Date() + "\0";
                    byte[] msg = message.getBytes();
                    DatagramPacket paquete = new DatagramPacket(msg, msg.length, recibido.getAddress(), recibido.getPort());
                    finalS.send(paquete);
                    System.out.println("Respuesta enviada");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Mantener la aplicación en ejecución
        while (true) {
            try {
                Thread.sleep(1000);  // Mantener el servidor activo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
