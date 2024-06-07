package Scenes.SongList;

import Components.Menu.EventMenu;
import Components.Menu.GameMenu;
import Components.Menu.Menu3D;
import Scenes.OnePlayerScene;
import Scenes.TwoPlayerScene;
import Utilities.Song;

import javax.swing.*;
import java.awt.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
public class SongList extends JPanel {
    private String selectedSong;
    private Menu3D menu;
    Panel panel = new Panel();
    private int players;

    public SongList(JFrame frame, ArrayList<Song> songs, int WIDTH, int HEIGHT, int players){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(new Color(43, 45, 48));

        menu = new Menu3D();
        int menuWidth = (WIDTH / 6);
        int menuHeight = songs.size() * menu.getMenuHeight() + 75;
        menu.setPreferredSize(new Dimension(menuWidth, menuHeight));
        add(menu);
        add(panel);

        for (Song song : songs) {
            menu.addMenuItem(song.getName());
        }
        menu.addEvent(new EventMenu() {
            @Override
            public void menuSelected(int index) {
                // Obtener el nombre de la canción seleccionada
                selectedSong = songs.get(index).getName();
                // Actualizar el panel con los datos de la canción seleccionada
                updatePanel(selectedSong);
            }
        });

        menu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    selectedSong = songs.get(menu.getPressedIndex()).getName();
                    frame.getContentPane().removeAll();
                    if (players == 1) {
                        try{
                            OnePlayerScene onePlayerScene = new OnePlayerScene(frame,selectedSong);
                            frame.add(onePlayerScene);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                    } else {
                        try{
                            TwoPlayerScene twoPlayerScene = new TwoPlayerScene(frame,selectedSong);
                            frame.add(twoPlayerScene);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.getContentPane().removeAll();
                    GameMenu gameMenu = new GameMenu(frame, WIDTH, HEIGHT);
                    frame.add(gameMenu);
                    frame.revalidate();
                    frame.repaint();

                }
            }
        });
    }


    public String getSelectedSong() {
        return selectedSong;
    }

    public void updatePanel(String selectedSong){
        panel.removeAll();
        panel.add(new JLabel(selectedSong));
        panel.revalidate();
        panel.repaint();
    }
}
