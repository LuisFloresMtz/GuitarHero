package Scenes.Menu;

public class PauseMenu extends Menu3D{
    public PauseMenu() {
        super();
        items.removeAll(items);
        addMenuItem("Resume");
        addMenuItem("Restart");
        addMenuItem("Exit");
        addEvent(index -> {
            switch (index) {
                case 0:
                    System.out.println("Resume");
                    break;
                case 1:
                    System.out.println("Restart");
                    break;
                case 2:
                    System.out.println("Exit");
                    break;
            }
        });
    }
}
