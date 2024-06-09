package Scenes.SongList;

import Player.Tab;
import Scenes.Menu.EventMenu;
import Scenes.Menu.GameMenu;
import Scenes.Menu.Menu3D;
import Scenes.OnePlayerScene;
import Scenes.TwoPlayerScene;
import Utilities.Song;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
public class SongList extends JPanel {
    private String selectedSong;
    private Menu3D menu;
    private GameMenu gameMenu;
    private JFrame frame;
    Panel panel = new Panel();
    private int players;

    public SongList(GameMenu gameMenu, JFrame frame, ArrayList<Song> songs, int WIDTH, int HEIGHT, int players){
        System.out.println("SongList creado");
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(new Color(43, 45, 48));
        this.frame = frame;
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
                            //if(onePlayerScene == null)
                            OnePlayerScene onePlayerScene = new OnePlayerScene(gameMenu,frame,selectedSong);
                            frame.getContentPane().removeAll();
                            frame.getContentPane().add(onePlayerScene.getTab());
                            frame.revalidate();
                            frame.repaint();
                            //onePlayerScene.getTab().setMultiplayer(false);
                            onePlayerScene.getTab().play(selectedSong);
                            
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                    } else {
                        try{
                            TwoPlayerScene twoPlayerScene = new TwoPlayerScene(gameMenu,frame,selectedSong);
                            frame.add(twoPlayerScene.getTab());
                            frame.getContentPane().removeAll();
                            frame.getContentPane().add(twoPlayerScene.getTab());
                            frame.revalidate();
                            frame.repaint();
                            //twoPlayerScene.getTab().setMultiplayer(true);
                            twoPlayerScene.getTab().play(selectedSong);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    
                    gameMenu.resetMenu(frame);
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
    
    public void switchSongMenu() {
        frame.getContentPane().removeAll();
        frame.add(this);
        frame.revalidate();
        frame.repaint();
        SwingUtilities.invokeLater(menu::requestFocusInWindow);
    }
}
