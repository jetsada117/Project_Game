import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DemoServer extends JFrame{
    JLabel address = new JLabel();
    JPanel container = new JPanel();
    JPanel [] box = new JPanel[4];
    JLabel [] User = new JLabel[4];

    public DemoServer() {
        this.setSize(1000,600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        container.setLayout(new GridLayout(2,2,5,5));
        address.setFont(new Font("Tahoma", Font.BOLD, 20));

        add(address, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);

        for (int i = 0; i < User.length ; i++) {
            box[i] = new JPanel();
            box[i].setBackground(Color.cyan);
            box[i].setLayout(new BorderLayout());
            container.add(box[i]);

            User[i] = new JLabel("Player " + (i+1));
            User[i].setHorizontalAlignment(SwingConstants.CENTER);
            User[i].setFont(new Font("Tahoma", Font.BOLD, 30));
            box[i].add(User[i], BorderLayout.CENTER);
        }
    }

    public static void main(String[] args) {
        DemoServer server = new DemoServer();
        ServerThread thread = new ServerThread(server);
        thread.start();
        server.setVisible(true);

        try {
            InetAddress ip = InetAddress.getLocalHost();
            server.address.setText(ip.getHostAddress());
            server.address.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {}
    }
}


class ServerThread extends Thread {
    DemoServer server;
    PlayerServer [] playerServers = new PlayerServer[4]; 
    HashMap <String, Integer> players = new HashMap<>();
    int index = 0;

    public ServerThread(DemoServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        ServerSocket serverSock;

        try {
            serverSock = new ServerSocket(50101);

            while (true) {
                Socket socket = serverSock.accept();
                String clientIP = socket.getInetAddress().getHostAddress();
                InputStream input = socket.getInputStream();

                // รับค่าแบบเป็น object
                ObjectInputStream objectInput = new ObjectInputStream(input);
                Object receivedObject = objectInput.readObject();
                
                System.out.println("Connected with client IP: " + clientIP);

                try {       
                    if (receivedObject != null) {
                        // ตรวจสอบว่า object ที่รับเข้ามาเป็นประเภทใด เช่น Player
                        if (receivedObject instanceof Player playerob) {
                            System.out.println("Received object: " + playerob.getName());

                            if (!players.containsKey(playerob.getName()) && index < 4) {
                                // ตอน test เก็บค่าชื่อก่อน ตอนทำงานจริงค่อยเปลี่ยนเป็น ip
                                players.put(playerob.getName(), index);
                                server.User[index].setText(playerob.getName());
                                playerServers[index] = new PlayerServer();

                                index++;
                            }

                            if (players.containsKey(playerob.getName())) {
                                int i = players.get(playerob.getName());
                                playerServers[i].setName(playerob.getName());
                                playerServers[i].setX(playerob.getX());

                                for (int k = 0; k < index; k++) {
                                    if (k==i) continue;
                                
                                    playerServers[k].setX(playerob.getX());
                                }

                                PlayerThread thread = new PlayerThread(playerServers[i], index, clientIP);
                                thread.start();
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {}
    }   
}

class PlayerThread extends Thread {
    PlayerServer playerServer;
    int index;
    String clientIP;
    int x;
    String name;

    public PlayerThread(PlayerServer playerServer, int index, String clientIP) {
        this.playerServer = playerServer;
        this.index = index;
        this.clientIP = clientIP;
    }

    @Override
    public void run() {
        while (true) {
            x = playerServer.getX();
            name = playerServer.getName();

            playerServer.setX(x + 1);

            System.out.println("Player "+ name +" IP : "+ clientIP +" , Speed : "+ x);
           
            try (Socket socket = new Socket(clientIP, 5)) {
                ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
                objectOutput.writeObject(playerServer);
            } catch (IOException e1) {
                System.out.println(e1);
            }
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
        }
    }
}
