package Player;

import Connection.mysqlConnection;
import Scenes.Menu.GameMenu;
import Scenes.Menu.Menu3D;
import Scenes.Menu.PauseMenu;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;


import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

public class Tab extends JPanel {

    JFrame frame;
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
    long elapsedTime;
    long dt;
    Image backGround; 
    ImageIcon gifIcon;
    String selectedSong;


    public Tab(Player player, int playerNumber, String selectedSong, JFrame frame) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.player = player;
        setLayout(null);
        setPreferredSize(new Dimension((int) (screenSize.getWidth()), (int) screenSize.getHeight()));
        noteStreak.setBounds(screenSize.width - 145, 15, 100, 15);
        multiplier.setBounds(screenSize.width - 145, 30, 100, 15);
        score.setBounds(screenSize.width - 145, 45, 100, 15);
        life.setBounds(screenSize.width - 145, 60, 100, 15);
        greenNote.setBounds(xpos, ypos, 50, 35);
        redNote.setBounds(xpos + 75, ypos, 50, 35);
        yellowNote.setBounds(xpos + 150, ypos, 50, 35);
        blueNote.setBounds(xpos + 225, ypos, 50, 35);
        orangeNote.setBounds(xpos + 300, ypos, 50, 35);
        this.selectedSong = selectedSong;

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
        ng = new NoteGenerator("Note Generator",this, selectedSong);
        notes = ng.getNotes();
        JLabel gifLabel;
        this.frame = frame;

        /*try {
            backGround = ImageIO.read(new File("/C:/Users/Diego/Downloads/video.gif/"));
        } catch (IOException e) {  
            System.exit(1);
        }
        //notesInScreen = new CopyOnWriteArrayList<>();*/
        gifIcon = new ImageIcon("src/main/java/Resources/BackGrounds/Ella_y_yo.gif/");
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
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        Image gifImage = gifIcon.getImage();
        g2d.drawImage(gifImage, 0, 0, getWidth(), getHeight(), this);
        g2d.dispose();
        //Image gifImage = gifIcon.getImage();
        //g.drawImage(gifImage, 0, 0, getWidth(), getHeight(), this);
        //g.drawImage(backGround,(int)50,170 - 100, 100, 100, this);
        if (!paused) {
            int oldNoteStreak = player.noteStreak;
            drawLines(g, xpos, ypos);
            //g.fillRect(0, 0, screenSize.width, screenSize.height);
            if (!notes.isEmpty()) {
                paintNotes(g);
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

    public void paintNotes(Graphics g) {
        Iterator<GameNote> iterator = notes.iterator();
        //Iterator<GameNote> iterator = notesInScreen.iterator();
        while (iterator.hasNext()) {
        //for(GameNote element : notesInScreen) {
            GameNote element = iterator.next();
            /*if(elapsedTime >= element.getTime() - dt) {
                if (!element.isAdded() /*&& (elapsedTime) >= (element.getTime())) {
                    //element.setBounds(element.getX(), element.getY(), 50, 50);
                    //add(element);
                    element.setAdded(true);
                    //g.fillOval(element.getX(), element.getY(), 50, 50);
                    //System.out.println("TIEMPO TRANSCURRIDO: " + elapsedTime);
                    //System.out.println("TIEMPO DE APARICION DE NOTA: " + (element.getTime() - dt));

                }*/
                if(element.isInScreen()){
                    //element.setBounds(element.getX(), element.getY(), 50, 50);
                    g.setColor(element.getBorderColor());
                    g.fillOval(element.getX(), element.getY(), 50, 35);
                    //element.physics((double)ypos,(double)dt);
                }
            //}
            //synchronized (ng) {
                //if (element.getY() >= 50) {
                if (element.getY() >= ypos && element.getY() <= ypos + 100 && element.isInScreen()) {
                 
                    if ((greenNote.isReleased() && greenNote.isClicked() && element.getX() == xpos) ||
                            (redNote.isReleased() && redNote.isClicked() && element.getX() == xpos + 75) ||
                            (yellowNote.isReleased() && yellowNote.isClicked() && element.getX() == xpos + 150) ||
                            (blueNote.isReleased() && blueNote.isClicked() && element.getX() == xpos + 225) ||
                            (orangeNote.isReleased() && orangeNote.isClicked() && element.getX() == xpos + 300)) {
                        if(element.getX() == xpos) greenNote.setClicked(false);
                        if(element.getX() == xpos + 75) redNote.setClicked(false);
                        if(element.getX() == xpos + 150) yellowNote.setClicked(false);
                        if(element.getX() == xpos + 225) blueNote.setClicked(false);
                        if(element.getX() == xpos + 300) orangeNote.setClicked(false);
                        //iterator.remove();
                        //remove(element);
                        element.setInScreen(false);
                        element.setScored(true);
                        //notesInScreen.remove(element);
                        //notes.remove(element);
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
            //}
        }
    }

    public void KB(int playerNumber) {
        KeyListener kb = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }

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
                            greenNote.setClicked(true);
                            break;
                        case KeyEvent.VK_S:
                            redNote.setReleased(false);
                            redNote.setClicked(true);
                            break;
                        case KeyEvent.VK_D:
                            yellowNote.setReleased(false);
                            yellowNote.setClicked(true);
                            break;
                        case KeyEvent.VK_F:
                            blueNote.setReleased(false);
                            blueNote.setClicked(true);
                            break;
                        case KeyEvent.VK_G:
                            orangeNote.setReleased(false);
                            orangeNote.setClicked(true);
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
                        case KeyEvent.VK_SEMICOLON:
                            blueNote.setReleased(false);
                            break;
                        case KeyEvent.VK_QUOTE:
                            orangeNote.setReleased(false);
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
                    if (playerNumber == 1) {
                        handleControllerInput(currState, greenNote, redNote, yellowNote, blueNote, orangeNote);
                        if (currState.startJustPressed) {
                            togglePause();
                        }
                    } else if (playerNumber == 2) {
                        handleControllerInput(currState, greenNote, redNote, yellowNote, blueNote, orangeNote);
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
        ng.start();
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
