package Components.Menu;

import Editor.Editor;
import Components.SongList.SongList;
import Connection.Socket.Client;
import Components.Scenes.ControllerSelection;
import Utilities.Song;
import jnafilechooser.api.JnaFileChooser;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameMenu extends JPanel {

    private JnaFileChooser fileChooser = new JnaFileChooser();
    private int panelWidth;
    private int panelHeight = 300;
    private int WIDTH;
    private int HEIGHT;
    private String selectedSong;
    private Menu3D menu;
    private ArrayList<Song> songs = new ArrayList<>();
    private SongList songList;
    private Clip clip;

    public GameMenu(JFrame frame, int WIDTH, int HEIGHT) {

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        setBackground(new Color(43, 45, 48));
        panelWidth = frame.getWidth() / 4;

        menu = new Menu3D();
        menu.addMenuItem("Un jugador");
        menu.addMenuItem("Dos jugadores");
        menu.addMenuItem("En linea");
        menu.addMenuItem("Editar");
        menu.addMenuItem("Cerrar");

        int menuHeight = menu.getItemsSize() * menu.getMenuHeight() + 75;
        int menuWidth = WIDTH / 3;

        menu.setBounds((WIDTH - menuWidth) / 2, (HEIGHT - menuHeight) / 2, menuWidth, menuHeight);

        menu.addEvent(index -> {
            switch (index) {
                case 0:
                    switchToOnePlayerScene(frame);
                    break;
                case 1:
                    switchToTwoPlayerScene(frame);
                    break;
                case 2:
                    switchToOnline(frame);
                    break;
                case 3:
                    switchToEdit(frame);
                    break;
                case 4:
                    System.exit(0);
                    break;
            }
        });
        add(menu);
        try {
            playAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SongList getSongList(boolean multiplayer) {
        return songList;
    }

    private void switchToOnePlayerScene(JFrame frame) {
        try {

            songList = new SongList(this, frame, WIDTH, HEIGHT, 1);
            frame.getContentPane().removeAll();
            frame.add(songList);
            frame.revalidate();
            frame.repaint();
            menu.cleanup();
            menu = null;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void switchToTwoPlayerScene(JFrame frame) {
        try {
            ControllerSelection controllerSelection = new ControllerSelection(this, frame, getWidth(), getHeight());

            // ControllerState check moved to Menu3D


            frame.getContentPane().removeAll();
            songList = new SongList(this, frame, WIDTH, HEIGHT, 2);
            frame.add(songList);
            frame.revalidate();
            frame.repaint();
            menu.cleanup();
            menu = null;

        } catch (Exception ex) {
            ex.printStackTrace();
            frame.getContentPane().removeAll();
            frame.add(new SongList(this, frame, getWidth(), getHeight(), 2));
            frame.revalidate();
            frame.repaint();
        }
    }


    private void switchToEdit(JFrame frame) {
        Editor editor = new Editor(frame, this);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(editor);
        frame.revalidate();
        frame.repaint();
    }

    public void resetMenu(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.add(this);
        frame.revalidate();
        frame.repaint();
        SwingUtilities.invokeLater(menu::requestFocusInWindow);
    }

    public void playAudio() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        String audioFilePath = "src/main/java/Resources/Songs/Silence.wav";
        File audioFile = new File(audioFilePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }

    public void switchToOnline(JFrame frame) {
        Client client = new Client(this, frame, getWidth(), getHeight());
        frame.getContentPane().removeAll();
        frame.add(client);
        frame.revalidate();
        frame.repaint();
        menu.cleanup();
        menu = null;
    }
}
