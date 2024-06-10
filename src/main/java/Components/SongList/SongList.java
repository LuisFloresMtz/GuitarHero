package Components.SongList;

import Components.Menu.*;
import Components.Scenes.*;
import Utilities.Song;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.event.KeyEvent.VK_ENTER;

public class SongList extends JPanel {
    private String selectedSong;
    private final Menu3D menu;
    private final Panel panel = new Panel();
    ImageIcon albumCover;
    private final ControllerManager controllers;
    private volatile boolean listeningForController = true;
    private int players;
    private JFrame frame;
    private final ArrayList<Song> songs = new ArrayList<>();
    private GameMenu gameMenu;
    private Thread controllerThread;

    public SongList(GameMenu gameMenu, JFrame frame, int WIDTH, int HEIGHT, int players) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new GridLayout(1, 2));
        setBackground(new Color(43, 45, 48));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        this.frame = frame;
        this.players = players;
        this.gameMenu = gameMenu;
        loadSongs();

        controllers = new ControllerManager();
        controllers.initSDLGamepad();

        menu = new Menu3D();
        int menuWidth = (WIDTH / 6);
        int menuHeight = songs.size() * menu.getMenuHeight() + 75;
        menu.setPreferredSize(new Dimension(menuWidth, menuHeight));

        add(menu);
        add(panel);

        for (Song song : songs) {
            menu.addMenuItem(song.getName());
        }

        menu.setFocusable(true);
        menu.requestFocusInWindow();
        updatePanel(songs.get(menu.getPressedIndex()));

        menu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleInput(e.getKeyCode());
            }
        });

        startControllerListener();
    }

    private void startControllerListener() {
        controllerThread = new Thread(() -> {
            while (listeningForController) {
                ControllerState currState = controllers.getState(0);
                if (currState.isConnected) {
                    if (currState.dpadUpJustPressed) {
                        handleInput(KeyEvent.VK_UP);
                    }
                    if (currState.dpadDownJustPressed) {
                        handleInput(KeyEvent.VK_DOWN);
                    }
                    if (currState.startJustPressed || currState.aJustPressed) {
                        handleInput(KeyEvent.VK_ENTER);
                    }
                    if (currState.bJustPressed) {
                        handleInput(KeyEvent.VK_ESCAPE);
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        controllerThread.start();
    }

    public void handleInput(int key) {
        switch (key) {
            case VK_ENTER:
                selectedSong = songs.get(menu.getPressedIndex()).getName();
                if (players == 1) {
                    try {
                        frame.getContentPane().removeAll();
                        frame.add(new OnePlayerScene(gameMenu, frame, selectedSong));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else {
                    try {
                        frame.getContentPane().removeAll();
                        frame.add(new TwoPlayerScene(gameMenu, frame, selectedSong));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                frame.revalidate();
                frame.repaint();
                listeningForController = false;
                break;
            case KeyEvent.VK_ESCAPE:
                listeningForController = false;
                frame.getContentPane().removeAll();
                frame.getContentPane().add(gameMenu);
                frame.revalidate();
                frame.repaint();
                break;
            case KeyEvent.VK_UP:
                updatePanel(songs.get(menu.getPressedIndex()));
                break;
            case KeyEvent.VK_DOWN:
                updatePanel(songs.get(menu.getPressedIndex()));
                break;
        }
    }

    public String getSelectedSong() {
        return selectedSong;
    }

    public void updatePanel(Song song) {
        albumCover = new ImageIcon("src/main/java/Resources/Images/" + song.getName() + ".jpg");

        Image imgCover = albumCover.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel cover = new JLabel(new ImageIcon(imgCover));

        JLabel songName = new JLabel("Nombre: " + song.getName());
        songName.setForeground(Color.WHITE);
        songName.setFont(menu.getFont());

        JLabel artist = new JLabel("Artista: " + song.getBand());
        artist.setForeground(Color.WHITE);
        artist.setFont(menu.getFont());

        JLabel genre = new JLabel("Género: " + song.getGenre());
        genre.setForeground(Color.WHITE);
        genre.setFont(menu.getFont());

        JLabel difficulty = new JLabel("Dificultad: " + song.getDifficulty());
        difficulty.setForeground(Color.WHITE);
        difficulty.setFont(menu.getFont());

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.removeAll();

        panel.add(Box.createVerticalGlue());
        panel.add(cover);
        panel.add(songName);
        panel.add(artist);
        panel.add(genre);
        panel.add(difficulty);
        panel.add(Box.createVerticalGlue());

        panel.revalidate();
        panel.repaint();
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

    public void loadSongs() {
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
        }
    }
}
