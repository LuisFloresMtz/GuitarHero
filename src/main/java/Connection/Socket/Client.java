package Connection.Socket;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import javax.imageio.ImageIO;

public class Client extends JPanel {
    JLabel imageLabel;
    Socket socket;
    ObjectInputStream in;

    public Client(JFrame frame, int WIDTH, int HEIGHT) {
        frame.setCursor(Cursor.getDefaultCursor());
        setBackground(new Color(43, 45, 48));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());

        imageLabel = new JLabel();
        add(imageLabel, BorderLayout.CENTER);

        try {
            socket = new Socket("localhost", 5000);
            in = new ObjectInputStream(socket.getInputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        byte[] imageBytes = (byte[]) in.readObject();
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
                        ImageIcon icon = new ImageIcon(img);
                        imageLabel.setIcon(icon);
                        frame.revalidate();
                        frame.repaint();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
