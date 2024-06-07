package Player;

import java.awt.Color;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

public class Player {
    public Tab tab;
    int playerNumber;
    int life;
    int noteStreak;
    int multiplier;
    int powerPorcentage;
    int score;
    int xpos;
    String chartPath;
    String selectedSong;
    PlayerNote greenNote = new PlayerNote(new Color(54, 58, 59), new Color(8, 200, 3), new Color(8, 200, 3));
    PlayerNote redNote = new PlayerNote(new Color(54, 58, 59), new Color(163, 24, 24), new Color(163, 24, 24));
    PlayerNote yellowNote = new PlayerNote(new Color(54, 58, 59), new Color(254, 254, 53), new Color(254, 254, 53));
    PlayerNote blueNote = new PlayerNote(new Color(54, 58, 59), new Color(63, 162, 211), new Color(63, 162, 211));
    PlayerNote orangeNote = new PlayerNote(new Color(54, 58, 59), new Color(217, 147, 53), new Color(217, 147, 53));
    final JLabel noteStreakLabel = new JLabel("Note Streak: 0");
    final JLabel multiplierLabel = new JLabel("Multiplier: 1x");
    final JLabel scoreLabel = new JLabel("Score: 0");
    final JLabel lifeLabel = new JLabel("Life: 50");

    public Player(String selectedSong, JFrame frame) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.life = 50;
        this.noteStreak = 0;
        this.multiplier = 1;
        this.powerPorcentage = 0;
        this.score = 0;
        this.selectedSong = selectedSong;
    }
    public Player(int playerNumber, String selectedSong, JFrame frame) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.playerNumber = playerNumber;
        this.life = 50;
        this.noteStreak = 0;
        this.multiplier = 1;
        this.powerPorcentage = 0;
        this.score = 0;
        this.selectedSong = selectedSong;
    }

    public void setChartPath(String chartPath) {
        this.chartPath = chartPath;
    }
    public String getChartPath() {
        return this.chartPath;
    }

    public void resetNoteStreak() {
        this.noteStreak = 0;
    }
    public void resetMultiplier() {
        this.multiplier = 1;
    }


}
