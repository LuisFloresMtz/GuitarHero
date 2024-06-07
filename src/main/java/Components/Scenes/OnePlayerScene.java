package Components.Scenes;

import Player.Player;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class OnePlayerScene extends JPanel {
    Player player;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public OnePlayerScene(JFrame frame, String selectedSong) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        this.player = new Player(selectedSong,frame);
        setLayout(new GridLayout(1, 1));
        setSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));
        add(player.tab);
        player.tab.play();
    }

}
