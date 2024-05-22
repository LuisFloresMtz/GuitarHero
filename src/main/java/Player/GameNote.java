
package Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GameNote extends Note {
    private int x;
    private int y;
    boolean added;

    public GameNote(Color color, Color borderColor) {
        super(color, borderColor);
    }

    
    public GameNote(Color color) {
        super(color);
        this.x = 10;
        this.y = 0;
    }

    
    public GameNote(int x, int y, Color color) {
        super(color, color);
        this.x = x;
        this.y = y;
        this.added = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
    public void physics() {
        y += 2;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
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