package Scenes;

import Player.Player;

import javax.swing.*;
import java.awt.*;

public class TwoPlayerScene extends JFrame {

    Player player1 = new Player();
    Player player2 = new Player();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public TwoPlayerScene() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        mainPanel.add(player1.tab);

        mainPanel.add(player2.tab);

        getContentPane().add(mainPanel);
    }
}
