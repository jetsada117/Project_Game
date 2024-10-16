import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

class ClientMessage extends JFrame implements ActionListener{
    JTextField ip = new JTextField();
    JTextField textString = new JTextField();
    JTextField textSpeed = new JTextField();
    JButton buttonConnect = new JButton("Send Connect");
    JButton buttonObject = new JButton("Send Object");

    public ClientMessage() {
        this.setSize(600,100);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        this.add(ip, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1,2));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));  // จัดเรียงปุ่มในแถวเดียวกัน
        
        buttonPanel.add(buttonConnect);
        buttonPanel.add(buttonObject);
        centerPanel.add(textString);
        centerPanel.add(textSpeed);

        this.add(centerPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        buttonConnect.addActionListener(this);
        buttonObject.addActionListener(this);
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