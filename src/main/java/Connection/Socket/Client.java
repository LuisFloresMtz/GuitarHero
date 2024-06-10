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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Date;

public class Client extends JPanel {
    TextField server;
    Button connect;
    String ip;
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    JLabel imageLabel;
    GameMenu gameMenu;

    public Client(GameMenu gameMenu, JFrame frame, int WIDTH, int HEIGHT) {
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

        connect.addActionListener(e -> handleConnection(frame));

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
            Server server = new Server(frame, gameMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void handleConnection(JFrame frame) {
        DatagramSocket s = null;  // Declarar fuera del bloque try
        try {
            s = new DatagramSocket(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (s != null) {
            System.out.println("Servidor Activo");

            DatagramSocket finalS = s;
            new Thread(() -> {
                try {
                    while (true) {
                        DatagramPacket recibido = new DatagramPacket(new byte[1024], 1024);
                        System.out.println("Esperando...");
                        finalS.receive(recibido);
                        System.out.println("Ha llegado una petición \n");
                        System.out.println("Procedente de :" + recibido.getAddress());
                        System.out.println("En el puerto :" + recibido.getPort());
                        String dato = new String(recibido.getData()).split("\0")[0];
                        System.out.println("Dato: " + dato);
                        System.out.println("Sirviendo la petición");

                        String message = "HORA DEL SERVIDOR " + new Date() + "\0";
                        byte[] msg = message.getBytes();
                        DatagramPacket paquete = new DatagramPacket(msg, msg.length, recibido.getAddress(), recibido.getPort());
                        finalS.send(paquete);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        while (true) {
            // Realiza otras tareas aquí
        }
    }






}


