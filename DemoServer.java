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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DemoServer extends JFrame {
    JLabel address = new JLabel();
    JPanel container = new JPanel();
    JPanel[] box = new JPanel[4];
    JLabel[] User = new JLabel[4];

    public DemoServer() {
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        container.setLayout(new GridLayout(2, 2, 5, 5));
        address.setFont(new Font("Tahoma", Font.BOLD, 20));

        add(address, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);

        for (int i = 0; i < User.length; i++) {
            box[i] = new JPanel();
            box[i].setBackground(Color.cyan);
            box[i].setLayout(new BorderLayout());
            container.add(box[i]);

            User[i] = new JLabel("Player " + (i + 1));
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
        } catch (UnknownHostException e) {}
    }
}

class ServerThread extends Thread {
    DemoServer server;
    Socket socket;
    ServerObject Serversob = new ServerObject();
    ArrayList<String> players = new ArrayList<>();
    int index = 0;

    public ServerThread(DemoServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        ServerSocket serverSock;
        try {
            serverSock = new ServerSocket(50060);
            while (true) {
                socket = serverSock.accept();
                String clientIP = socket.getInetAddress().getHostAddress();
                InputStream input = socket.getInputStream();

                ObjectInputStream objectInput = new ObjectInputStream(input);
                Object receivedObject = objectInput.readObject();

                System.out.println("Connected with client IP: " + clientIP);

                try {
                    if (receivedObject != null) {
                        if (receivedObject instanceof PlayerObject playerob) {
                            System.out.println("Received object: " + playerob.getName());

                            if (!players.contains(clientIP) && index < 4) {
                                players.add(clientIP);
                                Serversob.setIP(clientIP, index);
                                Serversob.setIndex(index);
                                System.out.println(clientIP);

                                index++;
                            }

                            if (players.contains(clientIP)) {
                                int i = players.indexOf(clientIP);
                                Serversob.setName(playerob.getName(), i);
                                Serversob.setReady(playerob.isReady(), i);
                                Serversob.setSkin(playerob.getSkin(), i);
                                Serversob.setPlayer(players.size());

                                for (int k = 0 ; k < players.size() ; k++)
                                {
                                    System.out.println("player["+ k +"] , skin : NO." + Serversob.getSkin(k));
                                    System.out.println(playerob.isReady());
                                }

                                if (Serversob.isReady(i)) {
                                    server.User[i].setText(Serversob.getName(i) + "(Ready)");
                                } else {
                                    server.User[i].setText(Serversob.getName(i) + "(Wait)");
                                }

                                for (int k = 0; k < players.size(); k++) {
                                    String IpAddress = players.get(k);
                                    Serversob.setIndex(k);
                                    System.out.println("IpAddress: " + IpAddress);

                                    try (Socket clientSocket = new Socket(IpAddress, 50065);
                                        ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream())) {

                                        objectOutput.writeObject(Serversob);

                                    } catch (IOException e1) {
                                        System.out.println("Error Output : " + e1);
                                    }
                                }
                            }
                        }

                        if (allPlayersReady()) {
                            for (int i = 0; i < players.size(); i++) {
                                String IpAddress = players.get(i);
                                System.out.println(IpAddress);
        
                                InetAddress ip = InetAddress.getLocalHost();
                                Serversob.setIPServer(String.valueOf(ip.getHostAddress()));
        
                                PlayerThread thread = new PlayerThread(Serversob, i, IpAddress, players.size(), socket);
                                thread.start();
                                System.out.println("xxxxxxxxxxx");
                            }
        
                            ReveicedThread thread = new ReveicedThread(Serversob);
                            thread.start();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error Received : " + e);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error Reveiced :" + e);
        }
    }

    boolean allPlayersReady() {
        int readyPlayers = 0;
        for (int i = 0; i < 4; i++) {
            if (Serversob.isReady(i)) {
                readyPlayers++;
            }
        }

        return readyPlayers == Serversob.getPlayer();
    }
}

class PlayerThread extends Thread {
    private final ServerObject Serversob;
    Socket socket;
    int index;
    int count = 5;
    int x;
    String clientIP;

    public PlayerThread(ServerObject Serversob, int index, String clientIP, int player, Socket socket) {
        this.Serversob = Serversob;
        this.index = index;
        this.clientIP = clientIP;
        this.socket = socket;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Stopwatch(this.Serversob, this.index), 5000, 1000);

        this.Serversob.setPlayer(player);
    }

    @Override
    public void run() {
            while (true) {
            if ((count < 0) && (Serversob.getsizePosition(index) != null)) {
                for (int i = 0; i < Serversob.getsizePosition(index) ; i++) {
                    if (Serversob.getPosition(index, i) != null) {
                        x = Serversob.getPosition(index, i);
                        Serversob.setPosition(index, i, x - 1);
                        
                        if (x - 1 < 250) {
                            Serversob.deletePosition(index, i);
                            Serversob.deleteword(index, i);
                            int score = Serversob.getScore(index);
                            Serversob.setScore(score, index);
                        }
                    }
                }
                
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            } else {
                Serversob.setCount(count--);
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            
            try {
                socket = new Socket(Serversob.getIP(index), 50065);
                
                Serversob.setIndex(index);
                ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
                objectOutput.writeObject(Serversob);
                objectOutput.flush();
            } catch (IOException e) {
                System.out.println("Error PlayerThread 1 : "+ e);
            }
        }
    }
}

class ReveicedThread extends Thread {
    private final ServerObject serverob;
    Socket socket;

    public ReveicedThread(ServerObject serverob) {
        this.serverob = serverob;
    }

    @Override
    public void run() {
        ServerSocket serverSock;
        try {
            serverSock = new ServerSocket(50070);
            while (true) {
                socket = serverSock.accept();
                InputStream input = socket.getInputStream();

                ObjectInputStream objectInput = new ObjectInputStream(input);
                Object receivedObject = objectInput.readObject();

                if (receivedObject instanceof PlayerAll playerAll) {
                    serverob.setIPServer(playerAll.getIPServer());
                    for (int i = 0; i < playerAll.getPlayer() ; i++) {
                        serverob.setScore(playerAll.getScore(i), i);
                        for (int j = 0; j < playerAll.getsizePosition(i) ; j++) {
                            if (playerAll.getPosition(i, j) == null) {
                                serverob.setpositiondate(i, playerAll.getpositiondate(i));
                                serverob.deletePosition(i, j);
                                serverob.deleteword(i, j);
                                serverob.setLaser(playerAll.isLaser(i), i);
                                serverob.setghostDate(i, playerAll.getghostDate(i));
                            }
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Receivedthread " + e);
        }
    }
}

class Stopwatch extends TimerTask {
    private final ServerObject serverob;
    private final int index;
    private int seconds = 0;
    private int minutes = 5;
    private int count = 0;
    private String word;
    private int countdown;

    String[] shortVocabulary = 
    {       "growth", "market", "energy", "safety", "health",
            "impact", "change", "rights", "supply", "demand",
            "travel", "export", "import", "survey", "status",
            "profit", "credit", "carbon", "crisis", "policy",
            "trade", "income", "budget", "people", "global",
            "labour", "public", "future", "border", "manage",
            "report", "result", "access", "nation", "sector",
            "support", "output", "target", "reform", "survey",
            "profit", "export", "input", "credit", "debate",
            "review", "access", "policy", "growth", "carbon",
            "ronnakit", "teeranai", "nattapong", "jetsada", "peerapong"
    };

    public Stopwatch(ServerObject serverob, int index) {
        this.serverob = serverob;
        this.index = index;
    }

    @Override
    public void run() {
        serverob.setMinutes(minutes);
        serverob.setSeconds(seconds);

        countdown = serverob.getScore(index);
        if (countdown > 18) countdown = 18;
        try {
            if ((seconds % ((countdown / 3) - 10) == 0) && (minutes < 5)) {
                System.out.println("Server Ghost time!");

                serverob.setSizePosition(index, count + 1);
                serverob.setY(index, 220 + (index * 130));
                serverob.setPosition(index, count, 1200);
                word = shortVocabulary[(int) (Math.random() * shortVocabulary.length)];
                serverob.setWord(index, count, word);
                count++;

                System.out.println("x : "+ serverob.getPosition(index, count) +" y : "+ serverob.getY(index) +" count : "+ serverob.getsizePosition(index));
            }
        } catch (Exception e) {}

        if (seconds > 0) {
            seconds--;
        } else {
            if (minutes > 0) {
                minutes--;
                seconds = 59;
            } else {
                for (int i = 0; i < serverob.getPlayer() ; i++) {
                    for (int k = 0; k < serverob.getsizePosition(i) ; k++) {
                        serverob.deletePosition(i, k);
                        serverob.deleteword(i, k);
                    }
                }
                cancel();
            }
        }
    }
}
