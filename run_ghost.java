import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class run_ghost extends JFrame implements KeyListener {
    Image imagesGhost1;
    String timeString;
    Image imageBg;
    Image[] imageCharacter;
    PlayerAll playerob;
    int minutes;
    int seconds;
    int index;
    JTextField check_text;
    String data = "input text";
    int J = 0;
    boolean bang = false;

    public run_ghost(PlayerAll playerob) {
        this.playerob = playerob;

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

        // เรียกใช้เธรดสำหรับเพิ่มรูปภาพ
        new ImageAdder(panel).start();

        try {
            InetAddress ip = InetAddress.getLocalHost();

            for (int i = 0; i < this.playerob.getPlayer(); i++) {
                if (ip.getHostAddress().equals(this.playerob.getIP(i))) {
                    index = i;
                    System.out.println("Index gameplay : " + index);
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("UnknowHost: " + e);
        }

        // System.out.println("skin : " + this.playerob.getSkin(index));
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

    // สร้างคลาส Thread สำหรับเพิ่มรูปภาพ
    class ImageAdder extends Thread {
        private final CirclePanel panel;

        public ImageAdder(CirclePanel panel) {
            this.panel = panel;
        }

        @Override
        public void run() {
            while (true) {
                seconds = playerob.getSeconds();
                minutes = playerob.getMinutes();

                panel.repaint();

                try {

                    if (playerob.hasPosition(index)) {
                        for (int i = 0; i < playerob.sizePosition(index); i++) {

                            if (playerob.getPosition(index, i) != null) {
                                if (data.equals(playerob.getWord(index, i))) {
                                    playerob.deletePosition(index, i);
                                    playerob.deleteword(index, i);
                                    data = "";
                                    J = i + 1;
                                    check_text.setText("");
                                    System.out.println("banggg!!!");
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }

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

            g.setColor(Color.GRAY); // ข้อคววามที่พิมพ์
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(data, 180, 250);

            if (bang == true && minutes != 0 && seconds != 0) {
                try {
                    if (J < playerob.sizePosition(index)) {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setColor(Color.RED);
                        g2d.setStroke(new BasicStroke(10.0f)); // ความหนา 5 พิกเซล
                        g2d.setColor(Color.RED);
                        g2d.drawLine(260, 260, playerob.getPosition(index, J), 260);
                    }
                } catch (Exception e) {
                    System.out.println("Laser" + e);
                }

                try {
                    Thread.sleep(30);
                    bang = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
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

            // วาดรูปภาพทั้งหมดจาก Array
            if (playerob.hasPosition(index)) {
                for (int i = 0; i < playerob.getPlayer(); i++) {
                    for (int k = 0; k < playerob.sizePosition(i); k++) {

                        if (null != playerob.getPosition(i, k)) {
                            g.drawImage(imagesGhost1, playerob.getPosition(i, k), playerob.getY(i), 85, 85, this);

                            // วาดคำที่อยู่บนรูปภาพจากอาเรย์ words
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
                Exit.setBorderPainted(false);// ตั้งค่าไม่ให้แสดงพื้นหลัง
                Exit.setContentAreaFilled(false);
                Exit.setFocusPainted(false);
                add(Exit);
                Exit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
            } else {
                JButton Exit0 = new JButton();
                Icon imageExit0 = new ImageIcon(
                        System.getProperty("user.dir") + File.separator + "Image" + File.separator + "ghost.png");
                Exit0.setBounds(1160, 9, imageExit0.getIconWidth(), imageExit0.getIconHeight());
                Exit0.setIcon(imageExit0);
                Exit0.setBorderPainted(false);// ตั้งค่าไม่ให้แสดงพื้นหลัง
                Exit0.setContentAreaFilled(false);
                Exit0.setFocusPainted(false);
                add(Exit0);
                Exit0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char ch = e.getKeyChar();
        data = check_text.getText();
        data += ch;

        bang = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}