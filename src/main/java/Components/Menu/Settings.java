package Components.Menu;

import Components.SongList.SongList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Settings extends Menu3D {
    private GameMenu gameMenu;
    private JFrame frame;

    public Settings(GameMenu mainMenu, JFrame frame, int WIDTH, int HEIGHT) {
        super();
        setBackground(new Color(43, 45, 48));
        items.clear();
        addMenuItem("Modificar velocidad");

        int menuHeight = getItemsSize() * getMenuHeight() + 75;
        int menuWidth = WIDTH / 3;

        setBounds((WIDTH - menuWidth) / 2, (HEIGHT - menuHeight) / 2, menuWidth, menuHeight);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.getContentPane().removeAll();
                    GameMenu gameMenu = new GameMenu(frame,WIDTH,HEIGHT);
                    frame.getContentPane().add(gameMenu);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });
    }
}
