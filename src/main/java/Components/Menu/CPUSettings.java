
package Components.Menu;

import Player.GameNote;
import Player.Tab;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CPUSettings extends JPanel {
    JFrame frame;
    GameMenu gameMenu;
    Menu3D menu;
            
    CPUSettings(JFrame frame, GameMenu mainMenu, int WIDTH, int HEIGHT) {
        this.gameMenu = mainMenu;
        this.frame = frame;
        setBackground(new Color(43, 45, 48));
        setLayout(null);
        menu = new Menu3D();
        menu.items.clear();
        menu.addMenuItem("Facil");
        menu.addMenuItem("Medio");
        menu.addMenuItem("Dificil");
        menu.addMenuItem("Extremo");
        menu.setFocusable(true);
        menu.requestFocusInWindow();
        int menuHeight = menu.getItemsSize() * menu.getMenuHeight() + 75;
        int menuWidth = WIDTH / 3;
        menu.setBounds((WIDTH - menuWidth) / 2, (HEIGHT - menuHeight) / 2, menuWidth, menuHeight);
        add(menu);
        menu.addEvent(index -> {
            switch (index) {
                case 0:
                    Tab.setPresition(25);
                    break;
                case 1:
                    Tab.setPresition(20);
                    break;
                case 2:
                    Tab.setPresition(15);
                    break;
                case 3:
                    Tab.setPresition(10);
                    break;
                case 4:
                    Tab.setPresition(1);
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
