package Scenes;

import Player.Player;

import javax.swing.*;
import java.awt.*;

public class OnePlayerScene extends JPanel {
    Player player1 = new Player();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public OnePlayerScene() {
        setLayout(new GridLayout(1, 1));
        setSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));
        add(player1.tab);
        player1.tab.play();
    }

}
