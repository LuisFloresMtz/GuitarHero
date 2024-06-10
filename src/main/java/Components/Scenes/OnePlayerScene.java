package Components.Scenes;

import Player.*;
import Components.Menu.GameMenu;
import Components.Menu.Menu3D;
import Components.SongList.SongList;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class OnePlayerScene {
    Player player;
    Tab tab;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public OnePlayerScene(GameMenu mainMenu, JFrame frame, String selectedSong) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Tab.setMultiplayer(false);
        this.player = new Player(1,selectedSong,frame);
        this.tab = new Tab(mainMenu, player, null ,selectedSong, frame);
        this.tab = new Tab(player, null ,selectedSong, frame);

        this.player = new Player(selectedSong,frame);
        setLayout(new GridLayout(1, 1));
        setSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));
        add(tab);
        tab.play();
    }

    public Tab getTab() {
        return tab;
    }


}
