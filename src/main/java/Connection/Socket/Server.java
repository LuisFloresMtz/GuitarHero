package Connection.Socket;

import java.awt.AWTException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket serverSocket;
    Socket clientSocket;
    ObjectOutputStream out;
    ObjectInputStream in;

    Server() {
        try {
            serverSocket = new ServerSocket(5000);
            clientSocket = serverSocket.accept();

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        byte[] imageBytes = new ScreenCapture().captureScreen();
                        out.writeObject(imageBytes);
                        out.flush();
                        Thread.sleep(1000); // Captura y envía la imagen cada segundo
                    }
                } catch (IOException | AWTException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Server();
    }
}
