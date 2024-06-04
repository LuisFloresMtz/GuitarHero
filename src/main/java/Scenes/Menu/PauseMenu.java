package Scenes.Menu;

import java.awt.*;

public class PauseMenu extends Menu3D{
    public PauseMenu() {
        super();
        setBackground(new Color(0, 0, 0, 0.5f));
        items.removeAll(items);
        addMenuItem("Resume");
        addMenuItem("Restart");
        addMenuItem("Exit");
    }
}
