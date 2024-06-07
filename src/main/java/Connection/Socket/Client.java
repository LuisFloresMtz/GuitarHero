package Connection.Socket;

import Components.Menu.GameMenu;
import Components.TextField.TextField;
import Utilities.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends JPanel {
    TextField server;
    Button connect;
    String ip;
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    JLabel messageLabel;

    public Client(JFrame frame, int WIDTH, int HEIGHT) {
        frame.setCursor(Cursor.getDefaultCursor());
        setBackground(new Color(43, 45, 48));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        ip = "";

        JLabel title = new JLabel("Ip del servidor:");
        title.setBounds(WIDTH / 2 - 50, (HEIGHT / 2) - 100, 200, 50);
        title.setForeground(Color.WHITE);
        add(title);

        server = new TextField();
        server.setBounds(WIDTH / 2 - 100, (HEIGHT / 2) - 50, 200, 50);
        add(server);

        connect = new Button("Conectar");
        connect.setBounds(WIDTH / 2 - 100, (HEIGHT / 2) + 25, 200, 50);
        add(connect);

        messageLabel = new JLabel();
        messageLabel.setBounds(WIDTH / 2 - 100, (HEIGHT / 2) + 100, 300, 50);
        messageLabel.setForeground(Color.WHITE);
        add(messageLabel);

        connect.addActionListener(e -> handleConnect(frame));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.getContentPane().removeAll();
                    GameMenu gameMenu = new GameMenu(frame, WIDTH, HEIGHT);
                    frame.getContentPane().add(gameMenu);
                    frame.revalidate();
                    frame.repaint();
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    Image image = toolkit.createImage(new byte[0]);
                    Cursor transparentCursor = toolkit.createCustomCursor(image, new Point(0, 0), "invisibleCursor");
                    frame.setCursor(transparentCursor);
                }
            }
        });

        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    public void handleConnect(JFrame frame) {
        ip = server.getText();
        if (ip.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese una dirección IP");
        } else {
            try {
                socket = new Socket(ip, 5000);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                JOptionPane.showMessageDialog(null, "Conectado al servidor");

                // Enviar un mensaje de confirmación de conexión al servidor
                out.writeObject("CONNECTION_OK");
                out.flush();

                // Leer el mensaje de respuesta del servidor
                String response = (String) in.readObject();
                messageLabel.setText(response);
                frame.revalidate();
                frame.repaint();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor");
            }
        }
    }
}
