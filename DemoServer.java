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
import java.util.Timer;
import java.util.TimerTask;
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
            server.address.setText("Server IP : " + ip.getHostAddress());
            server.address.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {}
    }
}


class ServerThread extends Thread {
    DemoServer server;
    ServerObject [] Serversob = new ServerObject[4]; 
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
                        if (receivedObject instanceof PlayerObject playerob) {
                            System.out.println("Received object: " + playerob.getName());

                            if (!players.contains(clientIP) && index < 4) {
                                // ตอน test เก็บค่าชื่อก่อน ตอนทำงานจริงค่อยเปลี่ยนเป็น ip
                                players.add(clientIP);
                                Serversob[index] = new ServerObject();
                                Serversob[index].setIP(clientIP);
                                Serversob[index].setIndex(index);
                                System.out.println(clientIP);

                                index++;
                            }

                            if (players.contains(clientIP)) {
                                int i = players.indexOf(clientIP);
                                Serversob[i].setName(playerob.getName());
                                Serversob[i].setReady(playerob.isReady());
                                System.out.println(playerob.isReady());

                                if(Serversob[i].isReady()) 
                                {
                                    server.User[i].setText(Serversob[i].getName() +"(Ready)");
                                }
                                else
                                {
                                    server.User[i].setText(Serversob[i].getName() +"(Wait)");
                                }

                                if(!Serversob[i].isReady()) 
                                {
                                    for (int k = 0; k < players.size(); k++) {
                                        System.out.println(k);
                                        String IpAddress = players.get(k);
                                        Serversob[k].setPlayer(players.size());
                                        
                                        for (int j = 0; j < players.size(); j++) {
                                            try (Socket clientSocket = new Socket(IpAddress, 5);
                                            ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream())) {
                                            
                                            objectOutput.writeObject(Serversob[j]);
                                            System.out.println("Output : " + IpAddress);
                                            System.out.println(Serversob[j].getIP());

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
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
                        System.out.println(IpAddress);
                        
                        PlayerThread thread = new PlayerThread(Serversob[i], i, IpAddress, players.size());
                        thread.start();
                    }
                }
            }
        } catch (Exception e) {}
    }   
}

class PlayerThread extends Thread {
    ServerObject Serversob;
    int index;    
    int x;
    int count = 5;
    String clientIP;
    String name;

    public PlayerThread(ServerObject Serversob, int index, String clientIP, int player) {
        this.Serversob = Serversob;
        this.index = index;
        this.clientIP = clientIP;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Stopwatch(Serversob), 0, 1000);

        Serversob.setPlayer(player);
    }

    @Override
    public void run() {
        while (true) {
            if (count<0) {
                x = Serversob.getX();
                name = Serversob.getName();

                Serversob.setX(x + 1);

                System.out.println("["+ index +"]Player "+ name +" IP : "+ clientIP +" , Speed : "+ x);
            }
            else
            {
                Serversob.setCount(count--);
                System.out.println(Serversob.getCount());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}

            }

            try (Socket socket = new Socket(clientIP, 5)) {
                ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
                objectOutput.writeObject(Serversob);
            } catch (IOException e1) {
                System.out.println("Error Output : "+ e1);
            }
        }
    }
}


class Stopwatch extends TimerTask {
    private ServerObject serverob;
    private int seconds = 0;
    private int minutes = 5;

    public Stopwatch(ServerObject serverob) {
        this.serverob = serverob;
    }

    @Override
    public void run() {
        serverob.setMinutes(minutes);
        serverob.setSeconds(seconds);

        if (seconds > 0) {
            seconds--; // ลดค่าวินาทีลง
        } else {
            if (minutes > 0) {
                minutes--;  // ลดค่านาทีเมื่อวินาทีเหลือ 0
                seconds = 59; // รีเซ็ตวินาทีเป็น 59 สำหรับนาทีถัดไป
            } else {
                // จัดการเมื่อการนับถอยหลังเสร็จสิ้น (สามารถเพิ่มตรรกะเพิ่มเติมได้)
                System.out.println("หมดเวลา!");
                cancel(); // หยุด Timer เมื่อการนับถอยหลังเสร็จสิ้น
            }
        }
    }
}
