package Player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

public class NoteGenerator extends Thread{
    final ArrayList<GameNote> notes;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int ypos = (int) screenSize.getHeight() - 100;
    int xpos = (int) (screenSize.getWidth() - 300) / 2;

    public NoteGenerator(String name, ArrayList<GameNote> notes) {
        super(name);
        this.notes = notes;
    }

    public NoteGenerator(String name) {
        super(name);
        notes = new ArrayList<GameNote>();
        start();
    }

    public ArrayList<GameNote> getNotes() {
        return notes;
    }

    
    @Override
    public void run() {
        double aux;
        while(true){
            try{
                Thread.sleep((long)(Math.random() * 1500));
            } catch (InterruptedException e){}
            synchronized (notes){
                aux = Math.random() * 5 + 1;
                switch((int)aux){
                    case 1 :
                        notes.add(new GameNote(xpos, 0, new Color(54, 58, 59), new Color(8, 200, 3)));
                        break;
                    case 2:
                        notes.add(new GameNote(xpos + 75, 0, new Color(54, 58, 59), new Color(163, 24, 24)));
                        break;
                    case 3:
                        notes.add(new GameNote(xpos + 150, 0, new Color(54, 58, 59), new Color(254, 254, 53)));
                        break;
                    case 4:
                        notes.add(new GameNote(xpos + 225, 0, new Color(54, 58, 59), new Color(63, 162, 211)));
                        break;
                    case 5:
                        notes.add(new GameNote(xpos + 300, 0, new Color(54, 58, 59), new Color(217, 147, 53)));
                        break;
                }
            }
        }
    }
}
