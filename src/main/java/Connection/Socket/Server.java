package Connection.Socket;

import Components.Scenes.TwoPlayerScene;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket serverSocket;
    Socket clientSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    JFrame frame;
    TwoPlayerScene twoPlayerScene;

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

                // Abrir TwoPlayerScene
                SwingUtilities.invokeLater(() -> {
                    try {
                        frame = new JFrame("Two Player Scene");
                        twoPlayerScene = new TwoPlayerScene(frame, "selectedSong"); // Reemplaza "selectedSong" por tu canción seleccionada
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.add(twoPlayerScene);
                        frame.pack();
                        frame.setVisible(true);
                    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                        e.printStackTrace();
                    }
                });

                new Thread(() -> {
                    try {
                        while (true) {
                            byte[] imageBytes = captureScreen();
                            out.writeObject(imageBytes);
                            out.flush();
                            Thread.sleep(1000); // Captura y envía la imagen cada segundo
                        }
                    } catch (IOException | AWTException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                System.out.println("Conexión no confirmada");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] captureScreen() throws AWTException, IOException {
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(screenFullImage, "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        return imageInByte;
    }
}
