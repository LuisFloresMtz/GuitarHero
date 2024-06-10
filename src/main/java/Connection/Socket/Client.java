package Connection.Socket;

import Components.Menu.GameMenu;
import Components.TextField.TextField;
import Utilities.Button;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    JLabel imageLabel;
    GameMenu gameMenu;

    public Client(GameMenu gameMenu,JFrame frame, int WIDTH, int HEIGHT) {
        frame.setCursor(Cursor.getDefaultCursor());
        setBackground(new Color(43, 45, 48));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        ip = "";
        this.gameMenu = gameMenu;

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

        JLabel lblServer = new JLabel("Soy el servidor");
        lblServer.setBounds(WIDTH / 2 - 50, (HEIGHT / 2) + 125, 200, 50);
        lblServer.setForeground(Color.WHITE);
        add(lblServer);

        lblServer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleServer(frame);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblServer.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblServer.setCursor(Cursor.getDefaultCursor());
            }
        });

        connect.addActionListener(e -> handleConnect(frame));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gameMenu.resetMenu(frame);
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    Image image = toolkit.createImage(new byte[0]);
                    Cursor transparentCursor = toolkit.createCustomCursor(image, new Point(0, 0), "invisibleCursor");
                    frame.setCursor(transparentCursor);
                }
            }
        });

        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    public void handleServer(JFrame frame) {
        try {
            JOptionPane.showMessageDialog(null, "Esperando al cliente...\n Tu ip es: " + java.net.InetAddress.getLocalHost().getHostAddress());
            Server server = new Server(frame,gameMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                out.writeObject("CONNECTION_OK");
                out.flush();

                handleScreenCapture(frame);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor");
            }
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    try {
                        out.writeObject(e.getKeyCode());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    int keyCodeToSend = -e.getKeyCode();
                    try {
                        out.writeObject(keyCodeToSend);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            new Thread(() -> {
                while (true) {
                    try {
                        byte[] imageBytes = (byte[]) in.readObject();
                        if (imageBytes != null) {
                            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
                            ImageIcon icon = new ImageIcon(img);
                            SwingUtilities.invokeLater(() -> {
                                imageLabel.setIcon(icon);
                                frame.revalidate();
                                frame.repaint();
                            });
                            System.out.println("Imagen recibida");
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void handleScreenCapture(JFrame frame) {
        imageLabel = new JLabel();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(imageLabel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }
}
