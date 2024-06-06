package Scenes;

import Scenes.SongList.SongList;
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

public class ControllerSelection extends JPanel {
    ImageIcon guitarImage;
    ImageIcon keyboardImage;
    ControllerManager controllers = new ControllerManager();
    int currentGuitarSelection = 1;
    int currentKeyboardSelection = 1;

    public ControllerSelection(JFrame frame, int WIDTH, int HEIGHT) {
        guitarImage = new ImageIcon("src/main/java/resources/Images/Guitar.png");
        keyboardImage = new ImageIcon("src/main/java/resources/Images/Keyboard.png");
        ControllerManager controllers = new ControllerManager();

        int pos0 = (WIDTH / 3) - 200;
        int pos1 = (WIDTH / 2) - 200;
        int pos2 = (2 * WIDTH / 3) - 200;
        int guitarLabelY = (HEIGHT - 200) / 2 - 125;
        int keyboardLabelY = (HEIGHT - 200) / 2 + 125;

        Image guitarImg = guitarImage.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        Image keyboardImg = keyboardImage.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);

        JLabel guitarLabel = new JLabel(new ImageIcon(guitarImg));
        JLabel keyboardLabel = new JLabel(new ImageIcon(keyboardImg));

        guitarLabel.setBounds(pos1, guitarLabelY, 200, 200);
        keyboardLabel.setBounds(pos1, keyboardLabelY, 200, 200);

        JLabel labelP1 = new JLabel("Jugador 1");
        JLabel labelP2 = new JLabel("Jugador 2");

        labelP1.setForeground(Color.WHITE);
        labelP2.setForeground(Color.WHITE);

        add(labelP1);
        add(labelP2);

        labelP1.setBounds(pos0, 100, 100, 50);
        labelP2.setBounds(pos2, 100, 100, 50);


        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        setBackground(new Color(43, 45, 48));

        guitarLabel.setBounds(pos0, guitarLabelY, 200, 200);
        keyboardLabel.setBounds(pos0, keyboardLabelY, 200, 200);

        add(guitarLabel);
        add(keyboardLabel);

        controllers.initSDLGamepad();

        setFocusable(true);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (currentKeyboardSelection < 2) {
                        currentKeyboardSelection++;
                    }
                    if (currentKeyboardSelection == 0) {
                        keyboardLabel.setBounds(pos0, keyboardLabelY, 200, 200);
                    } else if (currentKeyboardSelection == 1) {
                        keyboardLabel.setBounds(pos1, keyboardLabelY, 200, 200);
                    } else if (currentKeyboardSelection == 2) {
                        keyboardLabel.setBounds(pos2, keyboardLabelY, 200, 200);
                    }
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (currentKeyboardSelection > 0) {
                        currentKeyboardSelection--;
                    }
                    if (currentKeyboardSelection == 0) {
                        keyboardLabel.setBounds(pos0, keyboardLabelY, 200, 200);
                    } else if (currentKeyboardSelection == 1) {
                        keyboardLabel.setBounds(pos1, keyboardLabelY, 200, 200);
                    } else if (currentKeyboardSelection == 2) {
                        keyboardLabel.setBounds(pos2, keyboardLabelY, 200, 200);
                    }
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    initGame(frame);
                }
            }
        });


        new Thread(() -> {
            while (true) {
                ControllerState currState = controllers.getState(0);
                if (currState.isConnected) {
                    if (currState.startJustPressed){
                        initGame(frame);
                    }
                    if (currState.dpadUpJustPressed) {
                        if (currentGuitarSelection < 2) {
                            currentGuitarSelection++;
                        }
                        if (currentGuitarSelection == 0) {
                            guitarLabel.setBounds(pos0, guitarLabelY, 200, 200);
                            repaint();
                        }
                        if (currentGuitarSelection == 1) {
                            guitarLabel.setBounds(pos1, guitarLabelY, 200, 200);
                            repaint();
                        }
                        if (currentGuitarSelection == 2) {
                            guitarLabel.setBounds(pos2, guitarLabelY, 200, 200);
                            repaint();
                        }
                    }
                    if (currState.dpadDownJustPressed) {
                        if (currentGuitarSelection > 0) {
                            currentGuitarSelection--;
                        }
                        if (currentGuitarSelection == 0) {
                            guitarLabel.setBounds(pos0, guitarLabelY, 200, 200);
                            repaint();
                        }
                        if (currentGuitarSelection == 1) {
                            guitarLabel.setBounds(pos1, guitarLabelY, 200, 200);
                            repaint();
                        }
                        if (currentGuitarSelection == 2) {
                            guitarLabel.setBounds(pos2, guitarLabelY, 200, 200);
                            repaint();
                        }
                    }
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();}

    public void initGame(JFrame frame){
        if(currentGuitarSelection != currentKeyboardSelection && currentGuitarSelection != 0 && currentKeyboardSelection != 0){
            try {
                ArrayList<Song> songs = new ArrayList<>();

                File folder = new File("src/main/java/Resources/Charts/");
                File[] listOfFiles = folder.listFiles();

                if (listOfFiles != null) {

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
                    frame.add(new SongList(frame, songs, getWidth(), getHeight(), 2));
                    frame.revalidate();
                    frame.repaint();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
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
                    song.setBand(line.substring(8, line.length() - 1));
                } else if (line.startsWith("Difficulty = ")) {
                    song.setDifficulty(Integer.parseInt(line.substring(13)));
                } else if (line.startsWith("Genre = ")) {
                    song.setGenre(line.substring(8, line.length() - 1));
                }
            }
        }
        return song;
    }
}
