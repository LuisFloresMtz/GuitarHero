
package Player;

import Scenes.Menu.Menu3D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameThread extends JPanel implements Runnable {
    private boolean paused = false;
    long currentTime;
    Tab tab;
    Player player;
    Menu3D menu;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int ypos;
    int xpos; 
    private final JLabel noteStreak = new JLabel("Note Streak: 0");
    private final JLabel multiplier = new JLabel("Multiplier: 1x");
    private final JLabel score = new JLabel("Score: 0");
    private final JLabel life = new JLabel("Life: 50");
    PlayerNote greenNote;
    PlayerNote redNote;
    PlayerNote yellowNote;
    PlayerNote blueNote;
    PlayerNote orangeNote;
    ArrayList<GameNote> notes;
    
    public GameThread(Tab tab) {
        this.tab = tab;
        this.player = player;
        setLayout(null);
        setPreferredSize(new Dimension((int) (screenSize.getWidth()), (int) screenSize.getHeight()));
        ypos = (int) screenSize.getHeight() - 75;
        xpos = (int) (screenSize.getWidth() - 300) / 2;
      
        System.out.println(ypos);
        System.out.println(xpos);
        noteStreak.setBounds(screenSize.width - 145, 15, 100, 15);
        multiplier.setBounds(screenSize.width - 145, 30, 100, 15);
        score.setBounds(screenSize.width - 145, 45, 100, 15);
        life.setBounds(screenSize.width - 145, 60, 100, 15);
        notes = this.tab.getNotes();
        setFocusable(true);
    }

    public boolean isPaused() {
        return paused;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public Tab getTab() {
        return tab;
    }
    
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
            int oldNoteStreak = player.noteStreak;
            //drawLines(g, xpos, ypos);
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
    
    public void paintNotes(Graphics g) {
        Iterator<GameNote> iterator = notes.iterator();
        System.out.println("hola");
        while (iterator.hasNext()) {
            GameNote element = iterator.next();
            if (element.isInScreen()) {
                g.setColor(element.getBorderColor());
                g.fillOval(element.getX(), element.getY(), 50, 35);
            }
            if (element.getY() >= ypos && element.getY() <= ypos + 100 && element.isInScreen()) {

                if ((greenNote.isReleased() && greenNote.isClicked() && element.getX() == greenNote.getX()) ||
                        (redNote.isReleased() && redNote.isClicked() && element.getX() == redNote.getX()) ||
                        (yellowNote.isReleased() && yellowNote.isClicked() && element.getX() == yellowNote.getX()) ||
                        (blueNote.isReleased() && blueNote.isClicked() && element.getX() == blueNote.getX()) ||
                        (orangeNote.isReleased() && orangeNote.isClicked() && element.getX() == orangeNote.getX())) {
                    if (element.getX() == xpos) greenNote.setClicked(false);
                    if (element.getX() == xpos + 75) redNote.setClicked(false);
                    if (element.getX() == xpos + 150) yellowNote.setClicked(false);
                    if (element.getX() == xpos + 225) blueNote.setClicked(false);
                    if (element.getX() == xpos + 300) orangeNote.setClicked(false);
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
    
    @Override
    public void run() {
        System.out.println("holaaaaaaaaa");
        while (true) {
                if (!paused) {
                    //tab.draw();
                    this.repaint();
                    try {
                        TimeUnit.NANOSECONDS.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
    }
}
