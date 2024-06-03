package Player;

import Connection.mysqlConnection;
import Scenes.Menu.Menu3D;
import Scenes.Menu.PauseMenu;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Tab extends JPanel {

    //mysqlConnection connection;
    Player player;
    Menu3D menu;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int ypos = (int) screenSize.getHeight() - 100;
    int xpos = (int) (screenSize.getWidth() - 300) / 2;
    private final JLabel noteStreak = new JLabel("Note Streak: 0");
    private final JLabel multiplier = new JLabel("Multiplier: 1x");
    private final JLabel score = new JLabel("Score: 0");
    private final JLabel life = new JLabel("Life: 50");
    PlayerNote greenNote = new PlayerNote(new Color(54, 58, 59), new Color(8, 200, 3), new Color(8, 200, 3));
    PlayerNote redNote = new PlayerNote(new Color(54, 58, 59), new Color(163, 24, 24), new Color(163, 24, 24));
    PlayerNote yellowNote = new PlayerNote(new Color(54, 58, 59), new Color(254, 254, 53), new Color(254, 254, 53));
    PlayerNote blueNote = new PlayerNote(new Color(54, 58, 59), new Color(63, 162, 211), new Color(63, 162, 211));
    PlayerNote orangeNote = new PlayerNote(new Color(54, 58, 59), new Color(217, 147, 53), new Color(217, 147, 53));
    ArrayList<GameNote> notes;
    final NoteGenerator ng;
    private boolean paused = false;

    public Tab(Player player) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this(player, 1);
    }

    public Tab(Player player, int playerNumber) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.player = player;
        setLayout(null);
        setPreferredSize(new Dimension((int) (screenSize.getWidth()), (int) screenSize.getHeight()));
        noteStreak.setBounds(screenSize.width - 145, 15, 100, 15);
        multiplier.setBounds(screenSize.width - 145, 30, 100, 15);
        score.setBounds(screenSize.width - 145, 45, 100, 15);
        life.setBounds(screenSize.width - 145, 60, 100, 15);
        greenNote.setBounds(xpos, ypos, 50, 50);
        redNote.setBounds(xpos + 75, ypos, 50, 50);
        yellowNote.setBounds(xpos + 150, ypos, 50, 50);
        blueNote.setBounds(xpos + 225, ypos, 50, 50);
        orangeNote.setBounds(xpos + 300, ypos, 50, 50);

        add(noteStreak);
        add(multiplier);
        add(score);
        add(life);
        add(greenNote);
        add(redNote);
        add(yellowNote);
        add(blueNote);
        add(orangeNote);
        KB(playerNumber);
        setFocusable(true);
        ng = new NoteGenerator("Note Generator",this);
        notes = ng.getNotes();
    }

    //Setters

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    //Getters

    public int getYpos() {
        return ypos;
    }

    public int getXpos() {
        return xpos;
    }

    // Methods
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!paused) {
            int oldNoteStreak = player.noteStreak;
            drawLines(g, xpos, ypos);
            g.fillRect(0, 0, screenSize.width, screenSize.height);
            if (!notes.isEmpty()) {
                paintNotes();
            }
            if (oldNoteStreak != player.noteStreak) {
                noteStreak.setText("Note Streak: " + player.noteStreak);
            }
            score.setText("Score: " + player.score);
            multiplier.setText("Multiplier: " + player.multiplier + "x");
            life.setText("Life: " + player.life);
        }
    }

    private void drawLines(Graphics g, int xpos, int ypos) {
        g.setColor(Color.BLACK);
        g.drawLine(xpos - 25, 0, xpos - 25, ypos + 75);
        g.drawLine(xpos + 25, 0, xpos + 25, ypos);
        g.drawLine(xpos + 100, 0, xpos + 100, ypos);
        g.drawLine(xpos + 175, 0, xpos + 175, ypos);
        g.drawLine(xpos + 250, 0, xpos + 250, ypos);
        g.drawLine(xpos + 325, 0, xpos + 325, ypos);
        g.drawLine(xpos + 375, 0, xpos + 375, ypos + 75);

        g.setColor(new Color(128, 128, 128, 128));
        g.fillRect(xpos - 25, 0, 400, ypos + 75);
    }

    public void paintNotes() {
        Iterator<GameNote> iterator = notes.iterator();
        while (iterator.hasNext()) {
            GameNote element = iterator.next();
            element.setBounds(element.getX(), element.getY(), 50, 50);
            if (!element.isAdded()) {
                add(element);
                element.setAdded(true);
            }
            element.physics();
            synchronized (ng) {
                if (element.getY() >= ypos && element.getY() <= ypos + 50) {
                    if ((element.getColor().equals(greenNote.getBorderColor()) && greenNote.isReleased() && element.getX() == xpos) ||
                            (element.getColor().equals(redNote.getBorderColor()) && redNote.isReleased() && element.getX() == xpos + 75) ||
                            (element.getColor().equals(yellowNote.getBorderColor()) && yellowNote.isReleased() && element.getX() == xpos + 150) ||
                            (element.getColor().equals(blueNote.getBorderColor()) && blueNote.isReleased() && element.getX() == xpos + 225) ||
                            (element.getColor().equals(orangeNote.getBorderColor()) && orangeNote.isReleased() && element.getX() == xpos + 300)) {
                        iterator.remove();
                        remove(element);
                        player.score += 50 * player.multiplier;
                        player.noteStreak++;
                        if (player.noteStreak % 10 == 0 && player.multiplier <= 4) {
                            player.multiplier++;
                        }
                        if (player.life < 100) {
                            player.life += 5;
                        }
                    }
                } else if (element.getY() >= screenSize.height) {
                    iterator.remove();
                    remove(element);
                    player.resetNoteStreak();
                    player.resetMultiplier();
                    if (player.life == 0) {
                        System.exit(0);
                    } else {
                        player.life -= 5;
                    }
                }
            }
        }
    }

    public void KB(int playerNumber) {
        KeyListener kb = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (playerNumber == 1) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_A:
                            greenNote.setReleased(true);
                            break;
                        case KeyEvent.VK_S:
                            redNote.setReleased(true);
                            break;
                        case KeyEvent.VK_D:
                            yellowNote.setReleased(true);
                            break;
                        case KeyEvent.VK_F:
                            blueNote.setReleased(true);
                            break;
                        case KeyEvent.VK_G:
                            orangeNote.setReleased(true);
                            break;
                        case KeyEvent.VK_ESCAPE:
                            togglePause();
                            break;
                    }
                } else if (playerNumber == 2) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_J:
                            greenNote.setReleased(true);
                            break;
                        case KeyEvent.VK_K:
                            redNote.setReleased(true);
                            break;
                        case KeyEvent.VK_L:
                            yellowNote.setReleased(true);
                            break;
                        case KeyEvent.VK_SEMICOLON:
                            blueNote.setReleased(true);
                            break;
                        case KeyEvent.VK_QUOTE:
                            orangeNote.setReleased(true);
                            break;
                        case KeyEvent.VK_ESCAPE:
                            togglePause();
                            break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (playerNumber == 1) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_A:
                            greenNote.setReleased(false);
                            break;
                        case KeyEvent.VK_S:
                            redNote.setReleased(false);
                            break;
                        case KeyEvent.VK_D:
                            yellowNote.setReleased(false);
                            break;
                        case KeyEvent.VK_F:
                            blueNote.setReleased(false);
                            break;
                        case KeyEvent.VK_G:
                            orangeNote.setReleased(false);
                            break;
                    }
                } else if (playerNumber == 2) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_J:
                            greenNote.setReleased(false);
                            break;
                        case KeyEvent.VK_K:
                            redNote.setReleased(false);
                            break;
                        case KeyEvent.VK_L:
                            yellowNote.setReleased(false);
                            break;
                        case KeyEvent.VK_DEAD_TILDE:
                            blueNote.setReleased(false);
                            break;
                        case KeyEvent.VK_OPEN_BRACKET:
                            orangeNote.setReleased(false);
                            break;
                    }
                }
            }
        };
        this.addKeyListener(kb);
    }

    public void play() {
        Thread gameThread = new Thread(() -> {
            while (true) {
                if (!paused) {
                    draw();
                }
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        });
        gameThread.start();
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    private void draw() {
        repaint();
    }

    void togglePause() {
        paused = !paused;
        if (paused) {
            ng.pauseAudio();
            menu = new PauseMenu();
            int menuWidth = 600;
            int menuHeight = 500;
            menu.setBounds(500, 50, menuWidth, menuHeight);
            this.add(menu);
            menu.setVisible(true);
            menu.requestFocusInWindow();
        } else {
            ng.resumeAudio();
            this.remove(menu);
            this.requestFocusInWindow();
        }
        this.revalidate();
        this.repaint();
    }

}
