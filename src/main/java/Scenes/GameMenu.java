package Scenes;

import Player.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu extends JComponent {

    ActionListener btnOnePlayerListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //OnePlayerScene game = new OnePlayerScene();
                JFrame game = new JFrame();
                Player player1 = new Player();
                game.setName("One player");
                game.getContentPane().add(player1.tab);
                game.setVisible(true);
                game.setResizable(false);
                game.pack();
                player1.tab.play();
            } catch (Exception ex) {}

        }
    };

    ActionListener btnTwoPlayerListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            TwoPlayerScene game = new TwoPlayerScene();
            game.setName("Two player");
            game.setVisible(true);
        }
    };

    JButton btnOnePlayer = new JButton("One Player");
    JButton btnTwoPlayer = new JButton("Two player");

    public GameMenu(int WIDTH, int HEIGHT){

        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        btnOnePlayer.setBounds(100,100,100,50);
        btnTwoPlayer.setBounds(100,200,100,50);
        add(btnOnePlayer);
        add(btnTwoPlayer);

        btnOnePlayer.addActionListener(btnOnePlayerListener);
        btnTwoPlayer.addActionListener(btnTwoPlayerListener);
    }










}





