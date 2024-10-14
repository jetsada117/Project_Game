import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.PrintStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

class ClientMessage extends JFrame {
    JTextField ip = new JTextField();
    JTextField text = new JTextField();
    JButton button = new JButton("Send Message");

    public ClientMessage() {
        setSize(600,200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));

        add(ip, BorderLayout.NORTH);
        add(text, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        ClientMessage frame = new ClientMessage();
        frame.setVisible(true);

        frame.button.addActionListener((ActionEvent e) -> {
            try {
                Socket socket = new Socket(frame.ip.getText(), 50101);
                PrintStream data = new PrintStream(socket.getOutputStream());
                String message = "Jetsada" + "#" + frame.text.getText(); 

                data.println(message);
                data.close();
            } catch (Exception ex) {
                
            }
        });
    }
}