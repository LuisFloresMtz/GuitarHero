package Player;

import Scenes.Menu.GameMenu;
import Scenes.Menu.Menu3D;
import Scenes.Menu.PauseMenu;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Tab extends JPanel {
    
    JFrame frame;
    //mysqlConnection connection;
    Player player;
    Player player2;
    Clip clip;
    Menu3D menu;
    GameMenu mainMenu;
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
    boolean exit;
    GameThread gameThread;


    public Tab(GameMenu mainMenu, Player player, Player player2, String selectedSong, JFrame frame) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        System.out.println("TAB Creado");
        setLayout(new GridLayout(1, 1));
        setSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));
        this.player = player;
        this.player2 = player2;
        this.mainMenu = mainMenu;
        this.selectedSong = selectedSong;
        this.exit = false;
        setLayout(null);
        setPreferredSize(new Dimension((int) (screenSize.getWidth()), (int) screenSize.getHeight()));
        ypos = (int) screenSize.getHeight() - 75;
        xpos = (int) (screenSize.getWidth() - 300) / 2;
       
        if(multiplayer){
            xpos /= 2;
            //this.player2 = new Player(2, selectedSong,frame/*,xpos * 3,ypos*/);
            this.player2.setXpos(xpos * 3);
            this.player2.setYpos(ypos);
            this.player2.addComponents(this,screenSize.width - 145);
            //ng2 = new NoteGenerator("Note Generator", this, selectedSong, player2, xpos * 3, ypos);
            //notes2 = ng2.getNotes();
        }
        //this.player = new Player(1,selectedSong,frame/*,xpos,ypos*/);
        this.player.setXpos(xpos);
        this.player.setYpos(ypos);
        this.player.addComponents(this,70);
        //ng = new NoteGenerator("Note Generator", this, selectedSong, player, xpos, ypos);
        //notes = ng.getNotes();
        KB();
        setFocusable(true);
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

    public Dimension getScreenSize() {
        return screenSize;
    }

    public static boolean isMultiplayer() {
        return multiplayer;
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
       
            //int oldNoteStreak = player.noteStreak;
        drawLines(g, xpos, ypos);
        if (!notes.isEmpty()) {
            paintNotes(g,notes,player);
        }
        if(multiplayer) {
            if(!notes2.isEmpty())
                paintNotes(g,notes2,player2); 
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

        /*ControllerManager controllers = new ControllerManager();
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
        }).start();*/
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


    public void play(String selectedSong) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        gameThread = new GameThread(this);
        ng = new NoteGenerator("Note Generator", this, selectedSong, player, xpos, ypos);
        notes = ng.getNotes();
       
        if(multiplayer){
            ng2 = new NoteGenerator("Note Generator", this, selectedSong, player2, xpos * 3, ypos);
            notes2 = ng2.getNotes();
            ng2.start();
        }
        ng.start();
        playAudio();
        gameThread.start();
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    public void draw() {
        repaint();
    }
    
    public void playAudio() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        String audioFilePath= "src/main/java/Resources/Songs/"+selectedSong+".wav";
        File audioFile = new File(audioFilePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }
     public void resumeAudio() {
        //clip.setMicrosecondPosition(pausePosition);
        //startTime = System.nanoTime() - pausePosition;
        clip.start();
        //startTime = System.currentTimeMillis() - pausePosition / 1000;
        //paused = false;
    }

    public void pauseAudio() {
        //pausePosition = clip.getMicrosecondPosition();
        //pausePosition = (long) (elapsedTime * 1000000f);
        clip.stop();
       // paused = true;
        //wait();
    }
    void togglePause() {
        paused = !paused;
        if (paused) {
            
            ng.pauseG();
            if(multiplayer)
                ng2.pauseG();
            pauseAudio();
            //if(menu == null) 
            menu = new PauseMenu();
            //menu.setVisible(true);
            int menuWidth = 600;
            int menuHeight = 500;
            menu.setBounds(500, 50, menuWidth, menuHeight);
            this.add(menu);
            this.repaint();
            
            menu.addEvent(index -> {
                switch (index) {
                    case 0:
                        togglePause();
                        break;
                    case 1:
                    {
                        try {
                            gameThread.setExit(true);
                            ng.setExit(true);
                            if(multiplayer)
                                ng2.setExit(true);
                            this.remove(menu);
                            paused = !paused;
                            this.repaint();
                            play(selectedSong);
                        } catch (Exception e) {
                        }
                    }
                        break;

                    case 2:
                        paused = false;
                        gameThread.setExit(true);
                        ng.setExit(true);
                        //player.removeComponents(this);
                        if(multiplayer) {
                            ng2.setExit(true);
                            //player2.removeComponents(this);
                        }
                        mainMenu.getSongList(multiplayer).switchSongMenu();
                        break;
                }
            });
        } else {
            ng.resumeG();
            if(multiplayer)
                ng2.resumeG();
            resumeAudio();
            this.remove(menu);
        }
    }

}
