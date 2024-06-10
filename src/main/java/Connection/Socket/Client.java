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
import java.net.*;
import java.util.Date;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

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

        connect.addActionListener(e -> {
            try {
                handleConnection(frame);
            } catch (UnknownHostException ex) {
                throw new RuntimeException(ex);
            } catch (SocketException ex) {
                throw new RuntimeException(ex);
            }
        });

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


    private void handleConnection(JFrame frame) throws UnknownHostException, SocketException {
        DatagramSocket dgSocket;
        InetAddress destination;

        // Configuración del socket y dirección de destino
        dgSocket = new DatagramSocket(5000, InetAddress.getByName(ip));
        destination = InetAddress.getByName(ip);

        System.out.println("Cliente UDP activo, enviando mensajes al servidor...");

        Timer timer = new Timer();
        InetAddress finalDestination = destination;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    // Crear y enviar el mensaje
                    String saludo = "hola" + "\0";
                    byte[] msg = saludo.getBytes();
                    DatagramPacket datagram = new DatagramPacket(msg, msg.length, finalDestination, 5000);
                    dgSocket.send(datagram);
                    System.out.println("Dato enviado");

                    // Recibir la respuesta del servidor
                    byte[] msgR = new byte[1024];
                    datagram = new DatagramPacket(msgR, msgR.length);
                    dgSocket.receive(datagram);
                    String received = new String(datagram.getData()).split("\0")[0];
                    System.out.println("DATOS DEL DATAGRAMA: " + received);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Programar la tarea para que se ejecute cada 500 ms
        timer.scheduleAtFixedRate(task, 0, 500);
    }

}









