package Scenes;

import Player.NoteGenerator;

import Player.*;
import Scenes.Menu.GameMenu;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TwoPlayerScene {
    Tab tab;
    Player player1;
    Player player2;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public TwoPlayerScene(GameMenu mainMenu, JFrame frame,String selectedSong) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Tab.setMultiplayer(true);
        player1 = new Player(1, selectedSong, frame);
        player2 = new Player(2, selectedSong, frame);
        this.tab = new Tab(mainMenu, player1, player2, selectedSong, frame);
    }

    public Tab getTab() {
        return tab;
    }
    
    
}
