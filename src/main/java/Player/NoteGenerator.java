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
    long elapsedTime;
    long dt;
    long pausePosition;

    public NoteGenerator(String name, JPanel panel, String selectedSong) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        super(name);
        this.panel = panel;
        notes = new ArrayList<>();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ypos = (int) screenSize.getHeight() - 100;
        xpos = (int) (screenSize.getWidth() - 300) / 2;
        //readChartFile("src/main/java/Resources/Charts/cult_of_personality.chart");
        //loadAudio("src/main/java/Resources/Songs/Cult of Personality.wav");
        //readChartFile("src/main/java/Resources/Charts/la_resaka.chart");
        //loadAudio("src/main/java/Resources/Songs/La resaka.wav");
        readChartFile("src/main/java/Resources/Charts/"+selectedSong+".chart");
        loadAudio("src/main/java/Resources/Songs/"+selectedSong+".wav");
        //start();
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
            } else if (line.startsWith("{") && inNotesSection){
                continue;
            } else if(line.equals("}") && inNotesSection){
                inNotesSection = false;
            }else if (inNotesSection && !line.isEmpty()) {
                processNoteLine(line);
            }
        }
        reader.close();
    }

    private void processNoteLine(String line) {
        String[] parts = line.split(" = ");
        int time = (int)(Integer.parseInt(parts[0].trim()));
        // PARA LA RESAKA
        
        if(time >= 0 && time < 3840) {
            time = (int)((time * 60) / (86.083 * .480));
        } else if(time >= 3840 && time < 4800) {
            time = (int)((time * 60) / (85.887 * .480));
        } else if(time >= 4800) {
            time = (int)((time * 60) / (86.000 * .480));
        }
        //  PARA ELLA Y YO
        //time = (int)((time * 60) / (176.000 * .480));

        System.out.println("TIEMPO: " + time);
        String[] noteParts = parts[1].split(" ");
        int track = Integer.parseInt(noteParts[1].trim());
        System.out.println("BOTON: " + track);
        if (track >= 0 && track <= 4) {
            GameNote note = switch (track) {
                case 0 -> new GameNote(xpos, new Color(54, 58, 59), new Color(8, 200, 3), time); 
                case 1 -> new GameNote(xpos + 75, new Color(54, 58, 59), new Color(163, 24, 24), time);
                case 2 -> new GameNote(xpos + 150, new Color(54, 58, 59), new Color(254, 254, 53), time);
                case 3 -> new GameNote(xpos + 225, new Color(54, 58, 59), new Color(63, 162, 211), time);
                case 4 -> new GameNote(xpos + 300, new Color(54, 58, 59), new Color(217, 147, 53), time);
                default -> null;
            };
            if (note != null) {
                notes.add(note);
                note.setBounds(note.getX(), note.getY(), 50, 50);
                System.out.println("Nota añadida");
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
        startTime = System.nanoTime();
        System.out.println("TIEMPO INICIAL: " + startTime);
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
        dt = (long)(((ypos + 15)-GameNote.getSpeed()*GameNote.getSpeed()*10)/(GameNote.getSpeed())* 10) ;
        playAudio();
        long t1 = startTime;
        //System.out.println(t1);
        System.out.println("dt: " + dt);
            while(true) {
                long loopStartTime = System.nanoTime();
                elapsedTime = (System.nanoTime() - startTime)/1000000;
                //long et2 =  (System.nanoTime() - t1);
                //System.out.println(et2);
                //if(et2 >= 1000000000) {
                for (GameNote element : notes) {
                    if(!element.isInScreen()) {
                        if((elapsedTime >= element.getTime() - dt)){
                            if(!element.isScored() && element.getY() < (ypos + 100))
                                element.setInScreen(true);
                        }
                    } else {
                      element.physics(ypos, dt);
                    }

                }
                //t1 = et2;
                    //System.out.println("EN BLOQUE");
                //}

                try {
                    long loopEndTime = System.nanoTime(); // Marca de tiempo al final del loop
                    long loopDuration = loopEndTime - loopStartTime;
                    long sleepTime = 10000000 - loopDuration; // 10 ms en nanosegundos menos la duración del loop
                if (sleepTime > 0) {
                    TimeUnit.NANOSECONDS.sleep(sleepTime);
                }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
        }
    }
}
