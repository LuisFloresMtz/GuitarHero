
package Components.Menu;

import Player.GameNote;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SpeedSettings extends JPanel {
    JFrame frame;
    GameMenu gameMenu;
    Menu3D menu;
    
    SpeedSettings(JFrame frame,GameMenu mainMenu, int WIDTH, int HEIGHT) {
        this.gameMenu = mainMenu;
        this.frame = frame;
        setBackground(new Color(43, 45, 48));
        setLayout(null);
        menu = new Menu3D();
        menu.items.clear();
        menu.addMenuItem("Muy Lento");
        menu.addMenuItem("Lento");
        menu.addMenuItem("Medio");
        menu.addMenuItem("Rapido");
        menu.addMenuItem("Muy Rapido");
        menu.setFocusable(true);
        menu.requestFocusInWindow();
        int menuHeight = menu.getItemsSize() * menu.getMenuHeight() + 75;
        int menuWidth = WIDTH / 3;
        menu.setBounds((WIDTH - menuWidth) / 2, (HEIGHT - menuHeight) / 2, menuWidth, menuHeight);
        add(menu);
        menu.addEvent(index -> {
            switch (index) {
                case 0:
                    GameNote.setSpeed(1.5);
                    break;
                case 1:
                    GameNote.setSpeed(2.5);
                    break;
                case 2:
                    GameNote.setSpeed(3.5);                    
                    break;
                case 3:
                    GameNote.setSpeed(5); 
                    break;
                case 4:
                    GameNote.setSpeed(6); 
                    break;
            }
        });
        menu.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    Settings settings = new Settings(gameMenu, frame, getWidth(), getHeight());
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(settings);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });
        
    }
}
