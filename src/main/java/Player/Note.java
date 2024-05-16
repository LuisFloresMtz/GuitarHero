package Player;

import javax.swing.*;
import java.awt.*;

public class Note extends JButton {

    protected Color color;
    protected Color borderColor;
    protected int radius = 100;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        setBackground(color);
    }


    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }


    public Note(Color color){
        this.color = color;
        this.borderColor = borderColor;
        setContentAreaFilled(false);
        setEnabled(false);
        setBorderPainted(false);
    }
    


    public Note(Color color, Color borderColor) {
        setColor(color);
        this.borderColor = borderColor;
        setContentAreaFilled(false);
        setEnabled(false);
        setBorderPainted(false);
    }
}
