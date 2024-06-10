package Components.Scenes;

import Player.*;
import Components.Menu.GameMenu;
import Components.Menu.Menu3D;
import Components.SongList.SongList;
import Utilities.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class OnePlayerScene extends JPanel{
    Player player;
    Tab tab;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public OnePlayerScene(GameMenu mainMenu, JFrame frame, Song song) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Tab.setMultiplayer(false);
        this.player = new Player(1, song.getName(), frame);
        this.tab = new Tab(mainMenu, player, null ,song, frame);

        this.player = new Player(song.getName(),frame);
        setLayout(new GridLayout(1, 1));
        setSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));
        add(tab);
        tab.play(song.getName());
    }

    public Tab getTab() {
        return tab;
    }


}
