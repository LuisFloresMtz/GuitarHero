package Player;

public class Player {
    public Tab tab;
    int life;
    int noteStreak;
    int multiplier;
    int powerPorcentage;
    int score;


    public Player() {
        this.tab = new Tab(this);
        this.life = 50;
        this.noteStreak = 0;
        this.multiplier = 1;
        this.powerPorcentage = 0;
        this.score = 0;
    }

    public void resetNoteStreak() {
        this.noteStreak = 0;
    }
    public void resetMultiplier() {
        this.multiplier = 1;
    }



}
