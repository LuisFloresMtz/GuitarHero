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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class Client extends JPanel {
    int PUERTO_DEL_CLIENTE = 5001; // Cambiar si es necesario
    int PUERTO_DEL_SERVIDOR = 5000;
    TextField server;
    Button connect;
    String ip;
    DatagramSocket dgSocket;
    DatagramPacket datagram;
    InetAddress destination = null;
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
                ip = server.getText();
                handleConnection();
            } catch (Exception ex) {
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
            JOptionPane.showMessageDialog(null, "Esperando al cliente...\n Tu ip es: " + InetAddress.getLocalHost().getHostAddress());
            new Server(frame, gameMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleConnection() throws Exception {
        dgSocket = new DatagramSocket(PUERTO_DEL_CLIENTE);
        destination = InetAddress.getByName(ip);

        System.out.println("Cliente UDP activo, enviando mensajes al servidor...");

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    // Crear y enviar el mensaje
                    String saludo = "hola" + "\0";
                    byte[] msg = saludo.getBytes();
                    DatagramPacket datagram = new DatagramPacket(msg, msg.length, destination, PUERTO_DEL_SERVIDOR);
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
