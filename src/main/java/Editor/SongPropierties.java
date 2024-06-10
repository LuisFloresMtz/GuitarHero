
package Editor;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SongPropierties extends JPanel {
    private JTextField name;
    private JTextField artist;
    private JTextField difficulty;
    private JTextField genre;
    
    public SongPropierties(JFrame frame, Editor editor) {
        setLayout(new GridBagLayout());
        setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

       
        JLabel nameLabel = new JLabel("Nombre:");
        nameLabel.setForeground(Color.WHITE);
        name = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(name, gbc);

        
        JLabel artistLabel = new JLabel("Artista:");
        artistLabel.setForeground(Color.WHITE);
        artist = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(artistLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(artist, gbc);

        JLabel difficultyLabel = new JLabel("Dificultad:");
        difficultyLabel.setForeground(Color.WHITE);
        difficulty= new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(difficultyLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(difficulty, gbc);

        JLabel genreLabel = new JLabel("Género:");
        genreLabel.setForeground(Color.WHITE);
        genre = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(genreLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(genre, gbc);
        
        JButton acceptButton = new JButton("Aceptar");
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save(editor);
                frame.dispose();
            }
        });
        acceptButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                acceptButton.setBackground(new Color(47, 127, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                acceptButton.setBackground(Color.GRAY);
            }
        });
        acceptButton.setBackground(Color.GRAY); 
        acceptButton.setForeground(new Color(238, 238, 238));
        acceptButton.setBorderPainted(false);
        acceptButton.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.fill = GridBagConstraints.NONE;
        add(acceptButton, gbc);
    }
    private void save(Editor panel) {
        panel.setSongName(name.getText());
        panel.setArtist(artist.getText());
        try {
            Integer.parseInt(difficulty.getText());
            panel.setDifficulty(difficulty.getText());
        } catch (Exception e) {
            panel.setDifficulty("0");
        }
        panel.setGenre(genre.getText());
    }
}