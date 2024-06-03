package Scenes.Menu;

import Player.*;
import Scenes.OnePlayerScene;
import Scenes.TwoPlayerScene;
import jnafilechooser.api.JnaFileChooser;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GameMenu extends JPanel {

   JnaFileChooser fileChooser = new JnaFileChooser();

    public GameMenu(JFrame frame, int WIDTH, int HEIGHT) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        setBackground(new Color(43, 45, 48));

        Menu3D menu = new Menu3D();
        int menuWidth = 600;
        int menuHeight = 500;
        menu.setBounds(500, 50, menuWidth, menuHeight);

        menu.addEvent(index -> {
            switch (index) {
                case 0:
                    switchToOnePlayerScene(frame);
                    break;
                case 1:
                    switchToTwoPlayerScene(frame);
                    break;
                case 2:
                    switchToEdit(frame);
                    break;
                case 3:
                    System.exit(0);
                    break;
            }
        });


        add(menu);

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
    private void switchToEdit(JFrame frame) {

            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setTitle("Selecciona una cancion");

            if (fileChooser.showOpenDialog(null)) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile.getName().toLowerCase().endsWith(".wav")) {
                    if (fileChooser.getSelectedFile() != null) {
                        String path = fileChooser.getSelectedFile().getAbsolutePath();
                        String name = fileChooser.getSelectedFile().getName();
                        String newPath = "src/main/java/Resources/Songs/" + name;
                        try {
                            java.nio.file.Files.copy(java.nio.file.Paths.get(path), java.nio.file.Paths.get(newPath), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                        Editor editor = new Editor(newPath);
                        frame.getContentPane().removeAll();
                        frame.getContentPane().add(editor);
                        frame.revalidate();
                        frame.repaint();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Please select a WAV file.", "Invalid File Type", JOptionPane.ERROR_MESSAGE);
                }
            }


    }
}
