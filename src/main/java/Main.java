import Connection.mysqlConnection;
import Player.Player;
import Scenes.GameMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) throws Exception {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame game = new JFrame("Game");

        game.setPreferredSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));
        game.setResizable(false);
        game.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        GameMenu menu = new GameMenu(game,game.getWidth(),game.getHeight());

        game.getContentPane().add(menu);
        game.pack();
        game.setLocationRelativeTo(null);
        game.setVisible(true);

    }
}

