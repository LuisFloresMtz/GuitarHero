package Player;

import Connection.mysqlConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Tab extends JPanel {

    mysqlConnection connection;
    Player player;
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

    public Tab(Player player) {
        //connection = new mysqlConnection();
        this.player = player;
        setLayout(null);
        setPreferredSize(new Dimension((int) (screenSize.getWidth()), (int) screenSize.getHeight()));
        noteStreak.setBounds(screenSize.width-145, 15, 100, 15);
        multiplier.setBounds(screenSize.width-145, 30, 100, 15);
        score.setBounds(screenSize.width-145, 45, 100, 15);
        life.setBounds(screenSize.width-145, 60, 100, 15);
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
        KB();
        setFocusable(true);
        NoteGenerator ng = new NoteGenerator("Note Generator");
        notes = ng.getNotes();
    }

    @Override
    protected void paintComponent(Graphics g) {
        int oldNoteStreak = player.noteStreak;
        super.paintComponent(g);
        drawLines(g, xpos, ypos);
        g.fillRect(0, 0, screenSize.width, screenSize.height);
        if (!notes.isEmpty()) {
            paintNotes();
        }
        if (oldNoteStreak != player.noteStreak){
            noteStreak.setText("Note Streak: " + player.noteStreak);
        }
        score.setText("Score: " + player.score);
        multiplier.setText("Multiplier: " + player.multiplier + "x");
        life.setText("Life: " + player.life);
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
            if (element.getY() >= ypos && element.getY() <= ypos + 50) {
                if ((element.getColor().equals(greenNote.getColor()) && greenNote.isReleased()) ||
                        (element.getColor().equals(redNote.getColor()) && redNote.isReleased()) ||
                        (element.getColor().equals(yellowNote.getColor()) && yellowNote.isReleased()) ||
                        (element.getColor().equals(blueNote.getColor()) && blueNote.isReleased()) ||
                        (element.getColor().equals(orangeNote.getColor()) && orangeNote.isReleased())) {
                    iterator.remove();
                    player.score += 50 * player.multiplier;
                    player.noteStreak++;
                    if (player.noteStreak % 10 == 0 && player.multiplier <= 4) {
                        player.multiplier++;
                    }
                    if (player.life < 100) {
                        player.life += 5;
                    }
                }
            } else if (element.getY() > screenSize.height + 50) {
                iterator.remove();
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



    public void draw() throws Exception {
        SwingUtilities.invokeAndWait(this::repaint);
    }

    private void KB() {
        KeyListener kb = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
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
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
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
            }
        };
        this.addKeyListener(kb);
    }


    public void play() throws Exception {
        while (true) {
            draw();
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}

    