package Connection.Socket;

import Components.Scenes.TwoPlayerScene;
import Components.SongList.SongList;
import Utilities.Song;
import com.studiohartman.jamepad.ControllerManager;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    ServerSocket serverSocket;
    Socket clientSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    JFrame frame;
    private final ControllerManager controllers;

    public Server(JFrame frame) {
        this.frame = frame;
        controllers = new ControllerManager();
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("Esperando conexiones...");
            clientSocket = serverSocket.accept();

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            String message = (String) in.readObject();
            if ("CONNECTION_OK".equals(message)) {
                System.out.println("Conexión confirmada por el cliente");

                out.writeObject("Conexión establecida exitosamente");
                out.flush();

                SwingUtilities.invokeLater(() -> {
                    try {
                        ArrayList<Song> songs = new ArrayList<>();

                        File folder = new File("src/main/java/Resources/Charts/");
                        File[] listOfFiles = folder.listFiles();

                        if (listOfFiles != null) {
                            System.out.println("Number of files: " + listOfFiles.length);

                            for (File file : listOfFiles) {
                                if (file.isFile()) {
                                    try {
                                        Song song = readSongFromFile(file);
                                        songs.add(song);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            frame.getContentPane().removeAll();
                            frame.add(new SongList(frame, songs, frame.getWidth(), frame.getHeight(), 2, controllers));
                            frame.revalidate();
                            frame.repaint();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                new Thread(() -> {
                    try {
                        while (true) {
                            byte[] imageBytes = captureFrame(frame);
                            out.writeObject(imageBytes);
                            out.flush();
                        }
                    } catch (IOException | AWTException e) {
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

    private byte[] captureFrame(JFrame frame) throws AWTException, IOException {
        BufferedImage bufferedImage = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        frame.paint(g2d);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        return imageInByte;
    }

    private Song readSongFromFile(File file) throws IOException {
        Song song = new Song();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Name = ")) {
                    song.setName(line.substring(8, line.length() - 1));
                } else if (line.startsWith("Artist = ")) {
                    song.setBand(line.substring(10, line.length() - 1));
                } else if (line.startsWith("Difficulty = ")) {
                    song.setDifficulty(Integer.parseInt(line.substring(13)));
                } else if (line.startsWith("Genre = ")) {
                    song.setGenre(line.substring(9, line.length() - 1));
                }
            }
        }
        return song;
    }
}
