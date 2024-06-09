package Scenes;

import Player.*;
import Scenes.Menu.GameMenu;
import Scenes.Menu.Menu3D;
import Scenes.SongList.SongList;
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
    }

    public Tab getTab() {
        return tab;
    }
    
    
}
