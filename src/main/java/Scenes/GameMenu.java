package Scenes;

import Player.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu extends JPanel {

    JFileChooser fileChooser = new JFileChooser();
    JButton btnOnePlayer = new JButton("One Player");
    JButton btnTwoPlayer = new JButton("Two player");
    JButton btnEditor = new JButton("Editor");

    public GameMenu(JFrame frame, int WIDTH, int HEIGHT) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);  // Usar layout nulo para usar setBounds
        btnOnePlayer.setBounds(100, 100, 150, 50);
        btnTwoPlayer.setBounds(100, 200, 150, 50);
        btnEditor.setBounds(100, 300, 150, 50);
        add(btnOnePlayer);
        add(btnTwoPlayer);
        add(btnEditor);

        btnOnePlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToOnePlayerScene(frame);
            }
        });

        btnTwoPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToTwoPlayerScene(frame);
            }
        });

        btnEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditor();
            }
        });
    }

    private void switchToOnePlayerScene(JFrame frame) {
        try {
            Player player1 = new Player();
            OnePlayerScene onePlayerPanel = new OnePlayerScene();
            frame.getContentPane().removeAll();
            frame.getContentPane().add(onePlayerPanel);
            frame.revalidate();
            frame.repaint();
            player1.tab.play();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void switchToTwoPlayerScene(JFrame frame) {
        try {
            Player player1 = new Player();
            Player player2 = new Player();
            TwoPlayerScene twoPlayerPanel = new TwoPlayerScene();
            frame.getContentPane().removeAll();
            frame.getContentPane().add(twoPlayerPanel);
            frame.revalidate();
            frame.repaint();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openEditor() {
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("*.txt", "txt"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Select a song");
        fileChooser.showOpenDialog(null);

        if (fileChooser.getSelectedFile() != null) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            String name = fileChooser.getSelectedFile().getName();
            String newPath = "src/main/java/Resources/Songs/" + name;
            try {
                java.nio.file.Files.copy(java.nio.file.Paths.get(path), java.nio.file.Paths.get(newPath), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            //Editor editor = new Editor(String newPath);
        }
    }


}
