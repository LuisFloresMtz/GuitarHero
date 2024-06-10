package Components.Menu;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Menu3D extends JComponent {

    private final List<EventMenu> events = new ArrayList<>();
    protected final List<Menu3dItem> items = new ArrayList<>();
    private final int left = 60;
    private int pressedIndex = 0;
    private final int menuHeight = 50;

    public Menu3D() {
        setFocusable(true);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
        init();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleInput(e.getKeyCode());
            }
        });

        ControllerManager controllers = new ControllerManager();
        controllers.initSDLGamepad();

        new Thread(() -> {
            while (true) {
                ControllerState currState = controllers.getState(0); // 0 is the index of the first controller

                if (currState.isConnected) {
                    if (currState.dpadUpJustPressed) {
                        handleInput(KeyEvent.VK_UP);
                    }
                    if (currState.dpadDownJustPressed) {
                        handleInput(KeyEvent.VK_DOWN);
                    }
                    if (currState.startJustPressed) {
                        handleInput(KeyEvent.VK_ENTER);
                    }
                    if (currState.aJustPressed) {
                        handleInput(KeyEvent.VK_ENTER);
                    }
                }

                try {
                    Thread.sleep(50); // Sleep to avoid flooding the console
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void handleInput(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (pressedIndex > 0) {
                    pressedIndex--;
                    repaint();
                }
                break;
            case KeyEvent.VK_DOWN:
                if (pressedIndex < items.size() - 1) {
                    pressedIndex++;
                    repaint();
                }
                break;
            case KeyEvent.VK_ENTER:
                if (pressedIndex != -1) {
                    items.get(pressedIndex).getAnimator().show();
                    hideMenu(pressedIndex);
                    runEvent();

                }
                break;
        }
    }



    private void init() {
        setForeground(new Color(238, 238, 238));
    }

    public void addEvent(EventMenu event) {
        this.events.add(event);
    }

    private void runEvent() {
        for (EventMenu event : events) {
            event.menuSelected(pressedIndex);
        }
    }

    public void addMenuItem(String menu) {
        int y = items.size() * menuHeight + left;
        int shadowSize = 15;
        items.add(new Menu3dItem(this, left, y, menuHeight, shadowSize, menu));
    }

    private int getOverIndex(Point mouse) {
        int index = -1;
        for (Menu3dItem d : items) {
            index++;
            if (d.isMouseOver(mouse)) {
                return index;
            }
        }
        return -1;
    }

    private void hideMenu(int exitIndex) {
        for (int i = 0; i < items.size(); i++) {
            if (i != exitIndex) {
                items.get(i).getAnimator().hide();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        for (int i = items.size() - 1; i >= 0; i--) {
            if (i == pressedIndex) {
                items.get(pressedIndex).getAnimator().show();
                hideMenu(pressedIndex);
            }
            float angle = 150f;
            items.get(i).render(g2, 360 - angle, left, this);
        }
        g2.dispose();
        super.paintComponent(g);
    }

    public int getItemsSize() {
        return items.size();
    }

    public int getMenuHeight() {
        return menuHeight;
    }
    public int getPressedIndex() {
        return pressedIndex;
    }


}
