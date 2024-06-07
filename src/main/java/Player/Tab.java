package Player;

import Components.Menu.GameMenu;
import Components.Menu.Menu3D;
import Components.Menu.PauseMenu;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Tab extends JPanel {

    JFrame frame;
    //mysqlConnection connection;
    Player player;
    Player player2;
    Menu3D menu;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int ypos;
    int xpos;
    private final JLabel noteStreak = new JLabel("Note Streak: 0");
    private final JLabel multiplier = new JLabel("Multiplier: 1x");
    private final JLabel score = new JLabel("Score: 0");
    private final JLabel life = new JLabel("Life: 50");
    ArrayList<GameNote> notes;
    ArrayList<GameNote> notes2;
    NoteGenerator ng;
    NoteGenerator ng2;
    private boolean paused = false;
    long elapsedTime;
    long dt;
    Image backGround;
    ImageIcon gifIcon;
    String selectedSong;
    static boolean multiplayer;


    public Tab(Player player, Player player2, String selectedSong, JFrame frame) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.player = player;
        setLayout(null);
        setPreferredSize(new Dimension((int) (screenSize.getWidth()), (int) screenSize.getHeight()));
        ypos = (int) screenSize.getHeight() - 75;
        xpos = (int) (screenSize.getWidth() - 300) / 2;
        if(multiplayer) {
            this.player2 = player2;
            xpos /= 2;
            player2.noteStreakLabel.setBounds(screenSize.width - 145, 15, 100, 15);
            player2.noteStreakLabel.setForeground(Color.WHITE);
            player2.multiplierLabel.setBounds(screenSize.width - 145, 30, 100, 15);
            player2.multiplierLabel.setForeground(Color.WHITE);
            player2.scoreLabel.setBounds(screenSize.width - 145, 45, 100, 15);
            player2.scoreLabel.setForeground(Color.WHITE);
            player2.lifeLabel.setBounds(screenSize.width - 145, 60, 100, 15);
            player2.lifeLabel.setForeground(Color.WHITE);
            player2.greenNote.setBounds(xpos * 3, ypos, 50, 35);
            player2.redNote.setBounds(xpos * 3 + 75, ypos, 50, 35);
            player2.yellowNote.setBounds(xpos * 3 + 150, ypos, 50, 35);
            player2.blueNote.setBounds(xpos * 3 + 225, ypos, 50, 35);
            player2.orangeNote.setBounds(xpos * 3 + 300, ypos, 50, 35);
            add(player2.noteStreakLabel);
            add(player2.multiplierLabel);
            add(player2.scoreLabel);
            add(player2.lifeLabel);
            add(player2.greenNote);
            add(player2.redNote);
            add(player2.yellowNote);
            add(player2.blueNote);
            add(player2.orangeNote);
            ng2 = new NoteGenerator("Note Generator", this, selectedSong, player2, xpos * 3, ypos);
            notes2 = ng2.getNotes();
        }
        player.noteStreakLabel.setBounds(70, 15, 100, 15);
        player.noteStreakLabel.setForeground(Color.WHITE);
        player.multiplierLabel.setBounds(70, 30, 100, 15);
        player.multiplierLabel.setForeground(Color.WHITE);
        player.scoreLabel.setBounds(70, 45, 100, 15);
        player.scoreLabel.setForeground(Color.WHITE);
        player.lifeLabel.setBounds(70, 60, 100, 15);
        player.lifeLabel.setForeground(Color.WHITE);
        player.greenNote.setBounds(xpos, ypos, 50, 35);
        player.redNote.setBounds(xpos + 75, ypos, 50, 35);
        player.yellowNote.setBounds(xpos + 150, ypos, 50, 35);
        player.blueNote.setBounds(xpos + 225, ypos, 50, 35);
        player.orangeNote.setBounds(xpos + 300, ypos, 50, 35);
        this.selectedSong = selectedSong;

        add(player.noteStreakLabel);
        add(player.multiplierLabel);
        add(player.scoreLabel);
        add(player.lifeLabel);
        add(player.greenNote);
        add(player.redNote);
        add(player.yellowNote);
        add(player.blueNote);
        add(player.orangeNote);
        KB();
        setFocusable(true);
        ng = new NoteGenerator("Note Generator", this, selectedSong, player, xpos, ypos);
        notes = ng.getNotes();
        JLabel gifLabel;
        this.frame = frame;

        /*try {
            backGround = ImageIO.read(new File("/C:/Users/Diego/Downloads/video.gif/"));
        } catch (IOException e) {  
            System.exit(1);
        }
        //notesInScreen = new CopyOnWriteArrayList<>();*/
        //gifIcon = new ImageIcon("src/main/java/Resources/BackGrounds/Ella_y_yo.gif/");
        /*gifLabel = new JLabel(gifIcon);
        /*gifLabel.setBounds(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
        add(gifLabel);*/
    }

    //Setters

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public static void setMultiplayer(boolean multiplayer) {
        Tab.multiplayer = multiplayer;
    }

    //Getters

    public int getYpos() {
        return this.ypos;
    }

    public int getXpos() {
        return this.xpos;
    }

    public ArrayList<GameNote> getNotes() {
        return notes;
    }

    // Methods
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        /*Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        Image gifImage = gifIcon.getImage();
        g2d.drawImage(gifImage, 0, 0, getWidth(), getHeight(), this);
        g2d.dispose();*/
        //Image gifImage = gifIcon.getImage();
        //g.drawImage(gifImage, 0, 0, getWidth(), getHeight(), this);
        //g.drawImage(backGround,(int)50,170 - 100, 100, 100, this);
        if (!paused) {
            //int oldNoteStreak = player.noteStreak;
            drawLines(g, xpos, ypos);
            if (!notes.isEmpty()) {
                paintNotes(g,notes,player);
            }
            if(multiplayer) {
                if(!notes2.isEmpty())
                   paintNotes(g,notes2,player2);
            }
            /*if (oldNoteStreak != player.noteStreak) {
                noteStreak.setText("Note Streak: " + player.noteStreak);
            }
            score.setText("Score: " + player.score);
            multiplier.setText("Multiplier: " + player.multiplier + "x");
            life.setText("Life: " + player.life);*/
        }
    }

    private void drawLines(Graphics g, int xpos, int ypos) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

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
        if(multiplayer) {
            g.setColor(Color.BLACK);
            g.drawLine(xpos * 3 - 25, 0, xpos * 3 - 25, ypos + 75);
            g.drawLine(xpos * 3 + 25, 0, xpos * 3 + 25, ypos);
            g.drawLine(xpos * 3 + 100, 0, xpos * 3 + 100, ypos);
            g.drawLine(xpos * 3 + 175, 0, xpos * 3 + 175, ypos);
            g.drawLine(xpos * 3 + 250, 0, xpos * 3 + 250, ypos);
            g.drawLine(xpos * 3 + 325, 0, xpos * 3 + 325, ypos);
            g.drawLine(xpos * 3 + 375, 0, xpos * 3 + 375, ypos + 75);

            g.setColor(new Color(128, 128, 128, 128));
            g.fillRect(xpos * 3 - 25, 0, 400, ypos + 75);
        }
    }

    public void paintNotes(Graphics g, ArrayList<GameNote> notes, Player player) {
        Iterator<GameNote> iterator = notes.iterator();
        int oldNoteStreak = player.noteStreak;
        while (iterator.hasNext()) {
            GameNote element = iterator.next();
            if (element.isInScreen()) {
                g.setColor(element.getBorderColor());
                g.fillOval(element.getX(), element.getY(), 50, 35);
            }
            if (element.getY() >= ypos && element.getY() <= ypos + 100 && element.isInScreen()) {

                if ((player.greenNote.isReleased() && player.greenNote.isClicked() && element.getX() == player.greenNote.getX()) ||
                        (player.redNote.isReleased() && player.redNote.isClicked() && element.getX() == player.redNote.getX()) ||
                        (player.yellowNote.isReleased() && player.yellowNote.isClicked() && element.getX() == player.yellowNote.getX()) ||
                        (player.blueNote.isReleased() && player.blueNote.isClicked() && element.getX() == player.blueNote.getX()) ||
                        (player.orangeNote.isReleased() && player.orangeNote.isClicked() && element.getX() == player.orangeNote.getX())) {
                    if (element.getX() == player.greenNote.getX()) player.greenNote.setClicked(false);
                    if (element.getX() == player.redNote.getX()) player.redNote.setClicked(false);
                    if (element.getX() == player.yellowNote.getX()) player.yellowNote.setClicked(false);
                    if (element.getX() == player.blueNote.getX()) player.blueNote.setClicked(false);
                    if (element.getX() == player.orangeNote.getX()) player.orangeNote.setClicked(false);
                    element.setInScreen(false);
                    element.setScored(true);
                    player.score += 50 * player.multiplier;
                    player.noteStreak++;
                    if (player.noteStreak % 10 == 0 && player.multiplier <= 4) {
                        player.multiplier++;
                    }
                    if (player.life < 100) {
                        player.life += 5;
                    }
                }
            } else if (element.getY() >= screenSize.height && !element.isScored() && element.isInScreen()) {
                element.setInScreen(false);
                player.resetNoteStreak();
                player.resetMultiplier();
                if (player.life == 0) {
                    //System.exit(0);
                } else {
                    player.life -= 5;
                }
            }

        }
        if (oldNoteStreak != player.noteStreak) {
                player.noteStreakLabel.setText("Note Streak: " + player.noteStreak);
        }
        player.scoreLabel.setText("Score: " + player.score);
        player.multiplierLabel.setText("Multiplier: " + player.multiplier + "x");
        player.lifeLabel.setText("Life: " + player.life);
    }

    public void KB() {
        KeyListener kb = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_A:
                            player.greenNote.setReleased(true);
                            break;
                        case KeyEvent.VK_S:
                            player.redNote.setReleased(true);
                            break;
                        case KeyEvent.VK_D:
                            player.yellowNote.setReleased(true);
                            break;
                        case KeyEvent.VK_G:
                            player.blueNote.setReleased(true);
                            break;
                        case KeyEvent.VK_H:
                            player.orangeNote.setReleased(true);
                            break;
                        case KeyEvent.VK_ESCAPE:
                            togglePause();
                            break;
                    }
                //} else if (playerNumber == 2) {
                if(multiplayer) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_I:
                            player2.greenNote.setReleased(true);
                            break;
                        case KeyEvent.VK_O:
                            player2.redNote.setReleased(true);
                            break;
                        case KeyEvent.VK_P:
                            player2.yellowNote.setReleased(true);
                            break;
                        case KeyEvent.VK_NUMPAD7:
                            player2.blueNote.setReleased(true);
                            break;
                        case KeyEvent.VK_NUMPAD8:
                            player2.orangeNote.setReleased(true);
                            break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_A:
                            player.greenNote.setReleased(false);
                            player.greenNote.setClicked(true);
                            break;
                        case KeyEvent.VK_S:
                            player.redNote.setReleased(false);
                            player.redNote.setClicked(true);
                            break;
                        case KeyEvent.VK_D:
                            player.yellowNote.setReleased(false);
                            player.yellowNote.setClicked(true);
                            break;
                        case KeyEvent.VK_G:
                            player.blueNote.setReleased(false);
                            player.blueNote.setClicked(true);
                            break;
                        case KeyEvent.VK_H:
                            player.orangeNote.setReleased(false);
                            player.orangeNote.setClicked(true);
                            break;
                    }
                if(multiplayer) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_I:
                            player2.greenNote.setReleased(false);
                            player2.greenNote.setClicked(true);
                            break;
                        case KeyEvent.VK_O:
                            player2.redNote.setReleased(false);
                            player2.redNote.setClicked(true);
                            break;
                        case KeyEvent.VK_P:
                            player2.yellowNote.setReleased(false);
                            player2.yellowNote.setClicked(true);
                            break;
                        case KeyEvent.VK_NUMPAD7:
                            player2.blueNote.setReleased(false);
                            player2.blueNote.setClicked(true);
                            break;
                        case KeyEvent.VK_NUMPAD8:
                            player2.orangeNote.setReleased(false);
                            player2.orangeNote.setClicked(true);
                            break;
                    }
                }
            }
        };
        this.addKeyListener(kb);

        ControllerManager controllers = new ControllerManager();
        controllers.initSDLGamepad();

        new Thread(() -> {
            while (true) {
                ControllerState currState = controllers.getState(0);
                if (currState.isConnected) {
                    if (player.playerNumber == 1) {
                        handleControllerInput(currState, player.greenNote, player.redNote, player.yellowNote, player.blueNote, player.orangeNote);
                        if (currState.startJustPressed) {
                            togglePause();
                        }
                    } else if (player.playerNumber == 2) {
                        handleControllerInput(currState, player.greenNote, player.redNote, player.yellowNote, player.blueNote, player.orangeNote);
                        if (currState.startJustPressed) {
                            togglePause();
                        }
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

    private void handleControllerInput(ControllerState currState, PlayerNote greenNote, PlayerNote redNote, PlayerNote yellowNote, PlayerNote blueNote, PlayerNote orangeNote) {
        if (currState.a) {  // Green fret
            greenNote.setReleased(true);
        } else {
            greenNote.setReleased(false);
            greenNote.setClicked(true);
        }
        if (currState.b) {  // Red fret
            redNote.setReleased(true);
        } else {
            redNote.setReleased(false);
            redNote.setClicked(true);
        }
        if (currState.y) {  // Yellow fret
            yellowNote.setReleased(true);
        } else {
            yellowNote.setReleased(false);
            yellowNote.setClicked(true);
        }
        if (currState.x) {  // Blue fret
            blueNote.setReleased(true);
        } else {
            blueNote.setReleased(false);
            blueNote.setClicked(true);
        }
        if (currState.lb) {  // Orange fret
            orangeNote.setReleased(true);
        } else {
            orangeNote.setReleased(false);
            orangeNote.setClicked(true);
        }
    }


    public void play() {
        //GameThread hilo = new GameThread(this);
        //Thread gameThread = new Thread();
        Thread gameThread = new Thread(() -> {
            while (true) {
                if (!paused) {
                    draw();
                    try {
                        TimeUnit.NANOSECONDS.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        gameThread.start();
        ng.playAudio();
        ng.start();
        if(multiplayer) ng2.start();
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    public void draw() {
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
            menu.addEvent(index -> {
                switch (index) {
                    case 0:
                        togglePause();
                        break;
                    case 1:

                        break;
                    case 2:
                        frame.getContentPane().removeAll();
                        GameMenu menu = new GameMenu(frame, (int) screenSize.getWidth(), (int) screenSize.getHeight());
                        frame.getContentPane().add(menu);
                        frame.revalidate();
                        frame.repaint();
                        break;
                }
            });
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
