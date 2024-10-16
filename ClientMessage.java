import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class ClientMessage extends JFrame implements ActionListener{
    JTextField ip = new JTextField();
    JTextField textString = new JTextField();
    JTextField textSpeed = new JTextField();
    JTextArea textshow = new JTextArea();
    JButton buttonConnect = new JButton("Send Connect");
    JButton buttonObject = new JButton("Send Object");

    public ClientMessage() {
        this.setSize(600,100);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(1,2));

        JPanel container1 = new JPanel();
        container1.setLayout(new BorderLayout());

        JPanel container2 = new JPanel();
        container2.setLayout(new GridLayout(1, 1));

        container1.add(ip, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1,2));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));  // จัดเรียงปุ่มในแถวเดียวกัน
        
        buttonPanel.add(buttonConnect);
        buttonPanel.add(buttonObject);
        centerPanel.add(textString);
        centerPanel.add(textSpeed);

        container1.add(centerPanel, BorderLayout.CENTER);
        container1.add(buttonPanel, BorderLayout.SOUTH);

        container2.add(textshow);

        this.add(container1);
        this.add(container2);

        buttonConnect.addActionListener(this);
        buttonObject.addActionListener(this);

        clientThread thread = new clientThread(this);
        thread.start();
    }

    public static void main(String[] args) {
        ClientMessage frame = new ClientMessage();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonConnect) {
            System.out.println("button object");
            try (Socket socket = new Socket(ip.getText(), 50101)) {
                ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
                Player player = new Player(textString.getText());  // Object ที่ต้องการส่ง
                objectOutput.writeObject(player);
            } catch (IOException e1) {
                System.out.println(e);
            }
        }
        else if(e.getSource() == buttonObject)
        {
            try (Socket socket = new Socket(ip.getText(), 50101)) {
                ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
                Player player = new Player(textString.getText());  // Object ที่ต้องการส่ง
                player.setX(Integer.parseInt(textSpeed.getText()));
                objectOutput.writeObject(player);
            } catch (IOException e1) {
                System.out.println(e);
            }
        }
        
    }
}


class clientThread extends Thread{
    ClientMessage client;

    public clientThread(ClientMessage client) {
        this.client = client;
    }

    @Override
    public void run() {
        ServerSocket serverSock;
        try {
            serverSock = new ServerSocket(5);

            while (true) {
                Socket socket = serverSock.accept();

                InputStream input = socket.getInputStream();

                ObjectInputStream objectInput = new ObjectInputStream(input);
                Object receivedObject = objectInput.readObject();
            
                if (receivedObject instanceof PlayerServer playerServer) {
                    client.textshow.insert(playerServer.getName() +" , Speed : "+ playerServer.getX() +"\n", 0);
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        } 
    }
}