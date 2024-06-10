package Connection.Socket;

import Components.Menu.GameMenu;
import Components.SongList.SongList;
import Components.Scenes.OnePlayerScene;
import Utilities.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Server {
    JFrame frame;
    GameMenu gameMenu;
    Song selectedSong;
    public Server(JFrame frame, GameMenu gameMenu) {
        this.frame = frame;
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
                SongList songList = new SongList(gameMenu, frame, 800, 600, 1);
                selectedSong = songList.getSelectedSong();

                while (true) {
                    DatagramPacket recibido = new DatagramPacket(new byte[1024], 1024);
                    System.out.println("Esperando mensaje...");
                    finalS.receive(recibido);
                    System.out.println("Ha llegado una petición");
                    System.out.println("Procedente de: " + recibido.getAddress());
                    System.out.println("En el puerto: " + recibido.getPort());

                    String dato = new String(recibido.getData()).split("\0")[0];
                    System.out.println("Dato recibido: " + dato);

                    if (dato.equals("REQUEST_SONG_LIST")) {

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(bos);
                        oos.writeObject(selectedSong);
                        oos.flush();
                        byte[] songListBytes = bos.toByteArray();
                        DatagramPacket paquete = new DatagramPacket(songListBytes, songListBytes.length, recibido.getAddress(), recibido.getPort());
                        finalS.send(paquete);
                        System.out.println("Lista de canciones enviada.");
                    } else {
                        String selectedSongName = dato;

                        if (selectedSong != null) {
                            SwingUtilities.invokeLater(() -> {
                                frame.getContentPane().removeAll();
                                try {
                                    frame.add(new OnePlayerScene(gameMenu, frame, selectedSong));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (UnsupportedAudioFileException e) {
                                    throw new RuntimeException(e);
                                } catch (LineUnavailableException e) {
                                    throw new RuntimeException(e);
                                }
                                frame.revalidate();
                                frame.repaint();
                            });

                            String message = selectedSongName + "\0";
                            byte[] msg = message.getBytes();
                            DatagramPacket paquete = new DatagramPacket(msg, msg.length, recibido.getAddress(), recibido.getPort());
                            finalS.send(paquete);
                            System.out.println("Nombre de la canción enviada: " + message);
                        } else {
                            System.out.println("Canción seleccionada no encontrada.");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
