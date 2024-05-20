package Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Tab extends JPanel {

    Player player;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int ypos = (int) screenSize.getHeight() - 100;
    int xpos = (int) (screenSize.getWidth() - 300) / 2;
    PlayerNote greenNote = new PlayerNote(new Color(54, 58, 59), new Color(8, 200, 3), new Color(8, 200, 3));
    PlayerNote redNote = new PlayerNote(new Color(54, 58, 59), new Color(163, 24, 24), new Color(163, 24, 24));
    PlayerNote yellowNote = new PlayerNote(new Color(54, 58, 59), new Color(254, 254, 53), new Color(254, 254, 53));
    PlayerNote blueNote = new PlayerNote(new Color(54, 58, 59), new Color(63, 162, 211), new Color(63, 162, 211));
    PlayerNote orangeNote = new PlayerNote(new Color(54, 58, 59), new Color(217, 147, 53), new Color(217, 147, 53));
    ArrayList<GameNote> notes;

    public Tab(Player player) {
        this.player = player;
        setLayout(null);
        setPreferredSize(new Dimension((int) (screenSize.getWidth()), (int) screenSize.getHeight()));

        greenNote.setBounds(xpos, ypos, 50, 50);
        redNote.setBounds(xpos + 75, ypos, 50, 50);
        yellowNote.setBounds(xpos + 150, ypos, 50, 50);
        blueNote.setBounds(xpos + 225, ypos, 50, 50);
        orangeNote.setBounds(xpos + 300, ypos, 50, 50);

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
        super.paintComponent(g);
        drawLines(g, xpos, ypos);
        g.fillRect(0,0,screenSize.width,screenSize.height);
        if (!notes.isEmpty()) {
            paintNotes();
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
        ArrayList<GameNote> toRemove = new ArrayList<>();
        for (GameNote element : notes) {
            element.setBounds(element.getX(), element.getY(), 50, 50);
            if (!element.isAdded()) {
                add(element);
                element.setAdded(true);
            }
            element.physics();
            if (element.getY() == screenSize.getHeight()) {
                remove(element);
                toRemove.add(element);
            }
        }
        if (!toRemove.isEmpty()){
            notes.removeAll(toRemove);
            player.resetNoteStreak();
            player.resetMultiplier();
             if (player.life==0){
                System.exit(0);
            }else{
                 player.life -= 5;
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

    