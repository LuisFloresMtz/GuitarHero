package Components.Scenes;

import Components.Menu.GameMenu;
import Player.NoteGenerator;

import Player.*;
import Utilities.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TwoPlayerScene extends JPanel {
    Tab tab;
    Player player1;
    Player player2;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public TwoPlayerScene(GameMenu gameMenu, JFrame frame, Song song) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Tab.setMultiplayer(true);
        player1 = new Player(1, song.getName(), frame);
        player2 = new Player(2, song.getName(), frame);
        this.tab = new Tab(gameMenu, player1, player2, song, frame);
        setLayout(new GridLayout(1, 1));
        //setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));
        add(tab);
        tab.play(song.getName());
    }
}