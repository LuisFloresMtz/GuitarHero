package Utilities;

public class Song {
    private String name;
    private String band;
    private int difficulty;
    private String genre;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", band='" + band + '\'' +
                ", difficulty=" + difficulty +
                ", genre='" + genre + '\'' +
                '}';
    }
}

