package Components.Scenes;

import Player.Player;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TwoPlayerScene extends JPanel {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public TwoPlayerScene(JFrame frame, String selectedSong) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Player player1 = new Player(1, selectedSong, frame);
        Player player2 = new Player(2, selectedSong, frame);

        setLayout(new GridLayout(1, 2));
        setPreferredSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));


        add(player1.tab);
        add(player2.tab);

        SwingUtilities.invokeLater(() -> {
            player1.tab.requestFocusInWindow();
            player2.tab.requestFocusInWindow();
        });

        new Thread(player1.tab::play).start();
        new Thread(player2.tab::play).start();
    }
}
