package Utilities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {
    //private
    private Position position = new Position();
    private BufferedImage image;
    private int frame = 0;
    private int width;
    private int height;
    int tw;
    int th;
    int pX, pY;

    public Sprite(String spriteName, int spriteWidth,
                  int spriteHeight, int x, int y) {
        try {
            //image = ImageIO.read(ClassLoader.getSystemResourceAsStream(spriteName));
            image = ImageIO.read(getClass().getResourceAsStream(spriteName));
            width = spriteWidth;
            height = spriteHeight;
            tw = image.getWidth() / width;
            th = image.getHeight() / height;

            pX = x;
            pY = y;
        } catch (IOException ex) {
            //...
        }
    }
    public void setFrame(int index) {
        frame = index;
    }

    public void pintar(Graphics g) {
        int x = position.x;
        int y = position.y;

        x += pX;
        y += pY;

        int i = frame % tw;
        int j = frame / tw;
        g.drawImage(image, x, y, x + width,
                y + height, i * width,
                j * height, (i + 1) * width,
                (j + 1) * height,
                null);
    }
    public void setPosicion(Position p) {
        position.Set(p);
    }
    public void setPosition(int x, int y) {
        position.Set(x, y);
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}



