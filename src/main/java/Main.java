import Player.Player;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) throws Exception {
        
        try {
                JFrame game = new JFrame();
                game.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e) {
                     System.exit(0);
                    }
                });
                Player player1 = new Player();
                game.setName("One player");
                game.getContentPane().add(player1.tab);
                game.setVisible(true);
                game.setResizable(false);
                game.pack();
                player1.tab.play();
            } catch (Exception e) {}
        
       /* Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame game = new JFrame("Game");

        game.setPreferredSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));
        game.setResizable(false);
        game.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        GameMenu menu = new GameMenu(game.getWidth(),game.getHeight());

        game.getContentPane().add(menu);
        game.pack();
        game.setLocationRelativeTo(null);
        game.setVisible(true);*/

    }
}

