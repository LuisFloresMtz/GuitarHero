package Player;

import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public Menu() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(90, 90, 90, 10));

        JButton resumeButton = new JButton("Resume");

        resumeButton.addActionListener(e -> {
            Container parent = this.getParent();
            if (parent instanceof Tab) {
                ((Tab) parent).togglePause();
            }
        });

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        add(Box.createVerticalGlue());
        add(resumeButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(quitButton);
        add(Box.createVerticalGlue());

        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}

