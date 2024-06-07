package Scenes;

import Player.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class OnePlayerScene extends JPanel {
    Player player;
    Tab tab;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public OnePlayerScene(JFrame frame, String selectedSong) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Tab.setMultiplayer(false);
        this.player = new Player(1,selectedSong,frame);
        this.tab = new Tab(player, null ,selectedSong, frame);

        this.player = new Player(selectedSong,frame);
        setLayout(new GridLayout(1, 1));
        setSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));
        add(tab);
        tab.play();
    }

}
