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
    JTextArea textshow = new JTextArea();
    JButton buttonConnect = new JButton("Send Connect");
    JButton buttonObject = new JButton("Ready!");
    PlayerObject playerob = new PlayerObject();

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
        buttonPanel.setLayout(new GridLayout(1, 2));
        
        buttonPanel.add(buttonConnect);
        buttonPanel.add(buttonObject);
        centerPanel.add(textString);

        container1.add(centerPanel, BorderLayout.CENTER);
        container1.add(buttonPanel, BorderLayout.SOUTH);

        container2.add(textshow);

        this.add(container1);
        this.add(container2);

        buttonConnect.addActionListener(this);
        buttonObject.addActionListener(this);

        ClientThread1 thread = new ClientThread1(this);
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

            // สร้าง socket เพื่อส่งค่า
            try (Socket socket = new Socket(ip.getText(), 50101)) {
                ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
                
                // set ค่าลง object
                playerob.setName(textString.getText());

                // ส่งค่า object ออกไป
                objectOutput.writeObject(playerob);
            } catch (IOException e1) {
                System.out.println(e);
            }
        }
        else if(e.getSource() == buttonObject)
        {
            // สร้าง socket เพื่อส่งค่า
            try (Socket socket = new Socket(ip.getText(), 50101)) {
                ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
                
                // ถ้ากดเตรียมพร้อมให้ set true ลง ready
                playerob.setReady(true);

                // ส่งค่า object ออกไป
                objectOutput.writeObject(playerob);
            } catch (IOException e1) {
                System.out.println(e);
            }
        }
        
    }
}


class ClientThread1 extends Thread{
    ClientMessage client;
    String timeString; 

    public ClientThread1(ClientMessage client) {
        this.client = client;
    }

    @Override
    public void run() {
        ServerSocket serverSock;
        try {
            serverSock = new ServerSocket(5);

            while (true) {
                // รับค่าผ่าน  socket 
                try (Socket socket = serverSock.accept();
                    InputStream input = socket.getInputStream();
                    ObjectInputStream objectInput = new ObjectInputStream(input)) {

                    // อ่าน object
                    Object receivedObject = objectInput.readObject();

                // เช็คชนิดข้อมูลว่าเป็นชนิดข้อมูล SeverObject หรือไม่
                if (receivedObject instanceof ServerObject playerServer) {

                    // ถ้ากดเตรียมพร้อมทุกคนแล้วให้นับเลข แล้วแสดงเวลา
                    if (playerServer.isReady(0)) {
                        if (playerServer.getCount() > 0) 
                        {
                            client.textshow.insert(playerServer.getCount() + "\n", 0);
                        } 
                        else 
                        {
                            
                        }
                    } 
                    else 
                    {
                        // ถ้ายังไม่กดเตรียมพร้อมให้แสดง player ที่เข้ามา

                    }
                }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } 
    }
}