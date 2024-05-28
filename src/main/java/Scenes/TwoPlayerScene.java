package Scenes;

import Player.Player;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TwoPlayerScene extends JPanel {

    Player player1 = new Player(1);
    Player player2 = new Player(2);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public TwoPlayerScene() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        setLayout(new GridLayout(1,2));
        setSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));
        player1.tab.setXpos(150);
        player2.tab.setXpos(150);
        add(player1.tab);
        add(player2.tab);
        player1.tab.play();
        player2.tab.play();
    }
}
