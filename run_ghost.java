import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class run_ghost extends JFrame implements KeyListener {
    Image imagesGhost1;
    String timeString;
    Image imageBg;
    Image[] imageCharacter;
    PlayerAll playerob;
    Socket socket;
    int minutes;
    int seconds;
    int index;
    int score = 0;
    int ghost = 0;
    JTextField check_text;
    String data = "input text";
    int ghost_X;
    Timer T;

    public run_ghost(PlayerAll playerob, int index) {
        this.playerob = playerob;
        this.index = index;

        setTitle("");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        check_text = new JTextField();
        check_text.setSize(1, 1);
        check_text.setLocation(0, 0);
        check_text.addKeyListener(this);
        add(check_text);

        CirclePanel panel = new CirclePanel();
        panel.setBackground(Color.BLACK);
        add(panel);

        new ImageAdder(panel).start();

        imageBg = Toolkit.getDefaultToolkit()
                .getImage(System.getProperty("user.dir") + File.separator + "Image" + File.separator + "b.png");
        imagesGhost1 = Toolkit.getDefaultToolkit()
                .getImage(System.getProperty("user.dir") + File.separator + "Image" + File.separator + "ghost1.png");
        imageCharacter = new Image[4];

        for (int i = 0; i < 4; i++) {
            imageCharacter[i] = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator
                    + "Image" + File.separator + String.valueOf(i + 5) + ".png");
        }
    }

    class ImageAdder extends Thread {
        private final CirclePanel panel;

        public ImageAdder(CirclePanel panel) {
            this.panel = panel;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {}

                seconds = playerob.getSeconds();
                minutes = playerob.getMinutes();

                System.out.println("ghost : "+ ghost +" size : "+ playerob.getsizePosition(index));
                if (ghost < playerob.getsizePosition(index)) {
                    if (playerob.getPosition(index, ghost) != null) {
                        if (data.equals(playerob.getWord(index, ghost))) {
                            ghost_X = playerob.getPosition(index, ghost);
                            playerob.deletePosition(index, ghost);
                            playerob.deleteword(index, ghost);

                            check_text.setText("");
                            data = "";

                            playerob.setLaser(true, index);
                            
                            score = score + 1;
                            playerob.setScore(score, index);
                            sendData();  
                            panel.repaint();                                  
                        }
                    }
                    else {
                        ghost = ghost + 1;
                    }
                }
                panel.repaint();
            }
        }
    }

    class CirclePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setLayout(null);
            g.drawImage(imageBg, 0, 0, getWidth(), getHeight(), this);

            timeString = String.format("%02d:%02d", minutes, seconds);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("Time Remaining: " + timeString + " seconds", 420, 30);

            g.setColor(Color.GRAY);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(data, 180, 250 + (index * 130));

            for (int i = 0; i < playerob.getPlayer() ; i++) {
                if (playerob.isLaser(i)) {
                    try {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setColor(Color.RED);
                        g2d.setStroke(new BasicStroke(20.0f)); // ความหนา 20 พิกเซล
                        g2d.setColor(Color.RED);
                        g2d.drawLine(260, 275 + (i * 130), ghost_X, 275 + (i * 130));
                    } catch (Exception e) {
                        System.out.println("Laser" + e);
                    }
                }
            }

            for (int i = 0; i < playerob.getPlayer() ; i++) {
                if (playerob.isLaser(i) && minutes != 0 && seconds != 0) {
                    T = new Timer(500, evt -> {
                        for (int k = 0; k < playerob.getPlayer() ; k++) {
                            playerob.setLaser(false, k);
                        }
                        T.stop();
                        sendData();
                        System.out.println("ward");
                    });

                    T.start();

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        System.out.println("bang : " + e);
                    }
                }
            }

            for (int i = 0; i < playerob.getPlayer(); i++) {
                for (int k = 0; k < 4; k++) {
                    if (playerob.getSkin(i) == (k + 5)) {
                        g.drawImage(imageCharacter[k], 100, 220 + (i * 130), 150, 100, this);
                        break;
                    }
                }
            }

            for (int i = 0; i < playerob.getPlayer(); i++) {
                String name = playerob.getName(i);

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 25));
                g.drawString("" + name, 110, 210 + (i * 130));
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 25));


            if (playerob.getsizePosition(index) > 0) {
                for (int i = 0; i < playerob.getPlayer(); i++) {
                    for (int k = 0; k < playerob.getsizePosition(i) ; k++) {
                        if (playerob.getPosition(i, k) != null) {
                            g.drawImage(imagesGhost1, playerob.getPosition(i, k), playerob.getY(i), 85, 85, this);

                            g.setColor(Color.WHITE);
                            g.setFont(new Font("Arial", Font.BOLD, 25));
                            g.drawString(playerob.getWord(i, k), playerob.getPosition(i, k) + 10,
                                    playerob.getY(i) - 10);
                        }
                    }
                }
            }

            if (minutes == 0 && seconds == 0) {
                JButton Exit = new JButton();
                Icon imageExit = new ImageIcon(
                        System.getProperty("user.dir") + File.separator + "Image" + File.separator + "Exit.png");
                Exit.setBounds(515, 650, imageExit.getIconWidth(), imageExit.getIconHeight());
                Exit.setIcon(imageExit);
                Exit.setBorderPainted(false);
                Exit.setContentAreaFilled(false);
                Exit.setFocusPainted(false);
                add(Exit);
                Exit.addActionListener((ActionEvent e) -> {
                    System.exit(0);
                });
            } else {
                JButton Exit0 = new JButton();
                Icon imageExit0 = new ImageIcon(
                        System.getProperty("user.dir") + File.separator + "Image" + File.separator + "ghost.png");
                Exit0.setBounds(1160, 9, imageExit0.getIconWidth(), imageExit0.getIconHeight());
                Exit0.setIcon(imageExit0);
                Exit0.setBorderPainted(false);
                Exit0.setContentAreaFilled(false);
                Exit0.setFocusPainted(false);
                add(Exit0);
                Exit0.addActionListener((ActionEvent e) -> {
                    System.exit(0);
                });
            }


            for (int i = 0; i < playerob.getPlayer(); i++) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 25));
                g.drawString(playerob.getName(i) + " : " + playerob.getScore(i) + " Count", 800, (i * 30) + 80);

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {}
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        char ch = e.getKeyChar();
        data = check_text.getText();
        data += ch;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    void sendData() {
        try{
            socket = new Socket(playerob.getIPServer(), 50070);
            if (socket.isConnected()) {
                ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
                objectOutput.writeObject(playerob);
                objectOutput.flush();
            }
        } catch (IOException e) {
            System.out.println("Failed to connect or send data: " + e.getMessage());
        }
    }
}