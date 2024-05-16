package Player;

public class Player {
    public Tab tab;
    int life;
    int noteStreak;
    int multiplier;
    int powerPorcentage;


    public Player() {
        this.tab = new Tab();
        this.life = 50;
        this.noteStreak = 0;
        this.multiplier = 1;
        this.powerPorcentage = 0;
    }

}
