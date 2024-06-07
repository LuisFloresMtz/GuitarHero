package Components.SongList;

import Components.Menu.GameMenu;
import Components.Menu.Menu3D;
import Components.Scenes.OnePlayerScene;
import Components.Scenes.TwoPlayerScene;
import Utilities.Song;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class SongList extends JPanel {
    private String selectedSong;
    private final Menu3D menu;
    private final Panel panel = new Panel();
    ImageIcon albumCover;
    private final ControllerManager controllers;
    private volatile boolean listeningForController = true;

    public SongList(JFrame frame, ArrayList<Song> songs, int WIDTH, int HEIGHT, int players, ControllerManager c) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new GridLayout(1, 2));
        setBackground(new Color(43, 45, 48));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        controllers = c;

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
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    selectedSong = songs.get(menu.getPressedIndex()).getName();
                    if (players == 1) {
                        try {
                            frame.getContentPane().removeAll();
                            frame.add(new OnePlayerScene(frame, selectedSong));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        try {
                            frame.getContentPane().removeAll();
                            frame.add(new TwoPlayerScene(frame, selectedSong));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                    frame.revalidate();
                    frame.repaint();
                    listeningForController = false; // Stop listening for controller events
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.getContentPane().removeAll();
                    GameMenu gameMenu = new GameMenu(frame, WIDTH, HEIGHT);
                    frame.getContentPane().add(gameMenu);
                    frame.revalidate();
                    frame.repaint();
                    listeningForController = false; // Stop listening for controller events
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    updatePanel(songs.get(menu.getPressedIndex()));
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    updatePanel(songs.get(menu.getPressedIndex()));
                }
            }
        });

        // Start a thread to listen for controller events
        new Thread(() -> {
            while (listeningForController) {
                ControllerState currState = controllers.getState(0);
                if (currState.isConnected) {
                    if (currState.dpadUpJustPressed) {
                        menu.handleInput(KeyEvent.VK_UP);
                    }
                    if (currState.dpadDownJustPressed) {
                        menu.handleInput(KeyEvent.VK_DOWN);
                    }
                    if (currState.startJustPressed || currState.aJustPressed) {
                        menu.handleInput(KeyEvent.VK_ENTER);
                    }
                }
                try {
                    Thread.sleep(50); // Sleep to avoid flooding the console
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
}
