package Player;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NoteGenerator extends Thread {
    final ArrayList<GameNote> notes;
    JPanel panel;
    int ypos;
    int xpos;
    Clip clip;
    long startTime;
    long pausePosition;

    public NoteGenerator(String name, JPanel panel) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        super(name);
        this.panel = panel;
        notes = new ArrayList<>();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ypos = (int) screenSize.getHeight() - 100;
        xpos = (int) (screenSize.getWidth() - 300) / 2;
        readChartFile("src/main/java/Resources/Charts/cult_of_personality.chart");
        loadAudio("src/main/java/Resources/Songs/Cult of Personality.wav");
        start();
    }

    public ArrayList<GameNote> getNotes() {
        return notes;
    }

    public void readChartFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        boolean inNotesSection = false;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("[ExpertSingle]")) {
                inNotesSection = true;
            } else if (line.startsWith("[")) {
                inNotesSection = false;
            } else if (line.startsWith("{")) {
                inNotesSection = false;
            } else if (inNotesSection && !line.isEmpty()) {
                processNoteLine(line);
            }
        }
        reader.close();
    }

    private void processNoteLine(String line) {
        String[] parts = line.split(" = ");
        int time = Integer.parseInt(parts[0].trim());
        String[] noteParts = parts[1].split(" ");
        int track = Integer.parseInt(noteParts[1].trim());
        if (track >= 0 && track <= 4) {
            GameNote note = switch (track) {
                case 0 -> new GameNote(new Color(8, 200, 3), time);
                case 1 -> new GameNote(new Color(163, 24, 24), time);
                case 2 -> new GameNote(new Color(254, 254, 53), time);
                case 3 -> new GameNote(new Color(63, 162, 211), time);
                case 4 -> new GameNote(new Color(217, 147, 53), time);
                default -> null;
            };
            if (note != null) {
                notes.add(note);
                note.setBounds(xpos + track * 75, 0, note.getRadius(), note.getRadius());
            }
        }
    }

    private void loadAudio(String audioFilePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File audioFile = new File(audioFilePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
    }

    public void playAudio() {
        clip.start();
        startTime = System.currentTimeMillis();
    }

    public void resumeAudio() {
        clip.setMicrosecondPosition(pausePosition);
        clip.start();
        startTime = System.currentTimeMillis() - pausePosition / 1000;
    }

    public void pauseAudio() {
        pausePosition = clip.getMicrosecondPosition();
        clip.stop();
    }


    @Override
    public void run() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        playAudio();
        Timer timer = new Timer(50, e -> {
            long elapsedTime = System.currentTimeMillis() - startTime;
            synchronized (notes) {
                for (GameNote note : notes) {
                    if (elapsedTime >= note.getTime()) {
                        int newY = ypos - (int)((elapsedTime - note.getTime()) * 0.1); // Adjust the speed
                        note.setLocation(note.getX(), newY);
                    }
                }
                panel.repaint();
            }
        });
        timer.start();
    }
}
