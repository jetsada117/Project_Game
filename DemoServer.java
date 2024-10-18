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
import java.util.ArrayList;
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
    PlayerServer [] Serversob = new PlayerServer[4]; 
    ArrayList <String> players = new ArrayList<>();
    int index = 0;
    int ready = 0;

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

                            if (!players.contains(playerob.getName()) && index < 4) {
                                // ตอน test เก็บค่าชื่อก่อน ตอนทำงานจริงค่อยเปลี่ยนเป็น ip
                                players.add(playerob.getName());
                                server.User[index].setText(playerob.getName());
                                Serversob[index] = new PlayerServer();
                                Serversob[index].setIp(clientIP);

                                index++;
                            }

                            if (players.contains(playerob.getName())) {
                                int i = players.indexOf(playerob.getName());
                                Serversob[i].setName(playerob.getName());
                                Serversob[i].setReady(playerob.isReady());

                                if(Serversob[i].isReady()) 
                                {
                                    server.User[i].setText(Serversob[i].getName() +"(Ready)");
                                }
                                else
                                {
                                    server.User[i].setText(Serversob[i].getName() +"(Wait)");
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error Received : "+ e);
                }

                for (int i = 0; i < players.size() ; i++) {
                    if(Serversob[i].isReady())
                    {
                        ready++;
                    }
                    else 
                    {
                        ready = 0;
                        break;
                    }
                }

                if (ready == players.size()) {
                    for (int i = 0; i < players.size() ; i++) {
                        String IpAddress = players.get(i);
                        
                        PlayerThread thread = new PlayerThread(Serversob[i], i, Serversob[i].getIp(), players.size());
                        thread.start();
                    }
                }
            }
        } catch (Exception e) {}
    }   
}

class PlayerThread extends Thread {
    PlayerServer Serversob;
    int index;    
    int x;
    int count = 5;
    int player;
    String clientIP;
    String name;


    public PlayerThread(PlayerServer Serversob, int index, String clientIP, int player) {
        this.Serversob = Serversob;
        this.index = index;
        this.clientIP = clientIP;
        this.player = player;
    }

    @Override
    public void run() {
        while (true) {
            try (Socket socket = new Socket(clientIP, 5)) {
                ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
                objectOutput.writeObject(Serversob);
            } catch (IOException e1) {
                System.out.println("Error Output : "+ e1);
            }
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}

            if (count<0) {
                x = Serversob.getX();
                name = Serversob.getName();

                Serversob.setX(x + 1);

                System.out.println("["+ index +"]Player "+ name +" IP : "+ clientIP +" , Speed : "+ x);
            }
            else
            {
                System.out.println(Serversob.getCount());
                Serversob.setCount(--count);

                try {
                    Thread.sleep(player * 500);
                } catch (InterruptedException e) {}
            }
        }
    }
}
