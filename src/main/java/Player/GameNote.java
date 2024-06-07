
package Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GameNote extends Note {
    private double x;
    private double y;
    private boolean inScreen;
    private boolean scored;
    private int time;
    private static double speed = 3;

    public GameNote(Color color, Color borderColor) {
        super(color, borderColor);
    }

    public GameNote(int x, Color color, Color borderColor, int time) {
        super(color, borderColor);
        this.x = (double)x;
        this.y = 0;
        this.time = time;
        this.inScreen = false;
        this.scored = false;
    }
    
    public GameNote(Color color, int time) {
        super(color);
        this.x = 10;
        this.y = 0;
        this.time = time;
    }

    
    public GameNote(int x, int y, Color color, int time) {
        super(color, color);
        this.x = x;
        this.y = y;
        this.inScreen = false;
    }

    public int getX() {
        return (int)x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getY() {
        return (int)y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    
    public void physics(double ypos, double dt) {
        y += speed ;
        //y += (speed * (dt))/ypos;
    }

    public boolean isInScreen() {
        return inScreen;
    }

    public void setInScreen(boolean added) {
        this.inScreen = added;
    }

    public int getTime() {
        return time;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }

    public boolean isScored() {
        return scored;
    }

    public static void setSpeed(double speed) {
        GameNote.speed = speed;
    }

    public static double getSpeed() {
        return speed;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //  Paint Border
        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(getBackground());

        //  Border set 2 Pix
        g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, radius, radius);
        super.paintComponent(g);
    }
}