package Player;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChartReader {
    private List<Note> notes;

    public ChartReader() {
        notes = new ArrayList<>();
    }




    public List<Note> getNotes() {
        return notes;
    }

}
