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
    int PUERTO_DEL_CLIENTE;
    int PUERTO_DEL_SERVIDOR;
    TextField server;
    Button connect;
    String ip;
    DatagramSocket dgSocket;
    DatagramPacket datagram;
    InetAddress destination = null;
    JLabel imageLabel;
    GameMenu gameMenu;


    public Client(GameMenu gameMenu, JFrame frame, int WIDTH, int HEIGHT) {
        PUERTO_DEL_CLIENTE = PUERTO_DEL_SERVIDOR = 5000;
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
            JOptionPane.showMessageDialog(null, "Esperando al cliente...\n Tu ip es: " + java.net.InetAddress.getLocalHost().getHostAddress());
            Server server = new Server(frame, gameMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleConnection(JFrame frame) throws Exception {
        new Thread(() -> {
            String saludo = new String("hola" + "\0");
            byte msg[] = saludo.getBytes();
            try {
                dgSocket = new DatagramSocket(
                        PUERTO_DEL_CLIENTE,
                        InetAddress.getByName(ip));
            } catch (Exception e) {
                System.out.println(e.getMessage());;
            }
            try {
                destination = InetAddress.getByName(
                        ip);
            } catch (UnknownHostException uhe) {
                System.err.println("Host no encontrado : " + uhe);
                System.exit(-1);
            }
            byte msgR[] = new byte[1024];
            try{
            datagram = new DatagramPacket(msg, msg.length,
                    destination,
                    PUERTO_DEL_SERVIDOR);
            dgSocket.send(datagram);
            System.out.println("Dato enviado");
            datagram = new DatagramPacket(msgR, msgR.length);
            dgSocket.receive(datagram);
            String received = new String(datagram.getData());
            received = received.split("\0")[0];
            System.out.println("DATOS DEL DATAGRAMA: "
                    + received);
            }catch (Exception e){e.printStackTrace();}
            dgSocket.close();
            try {

                Thread.sleep(500);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}









