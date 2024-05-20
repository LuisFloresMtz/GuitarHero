package Scenes;

import Player.Player;

import javax.swing.*;
import java.awt.*;

public class OnePlayerScene extends JFrame {
    Player player1 = new Player();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public OnePlayerScene() {
        setSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));
        getContentPane().add(player1.tab);
        setResizable(false);

    }

}
