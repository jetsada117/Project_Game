import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class run_ghost extends JFrame {
    Image imagesGhost1;
    String timeString;
    Image imageBg;
    Image [] imageCharacter;
    PlayerAll playerob;
    int minutes;
    int seconds;
    int index;

    public run_ghost(PlayerAll playerob) {
        this.playerob = playerob;

        setTitle("");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        CirclePanel panel = new CirclePanel();
        panel.setBackground(Color.BLACK);
        add(panel);

        // เรียกใช้เธรดสำหรับเพิ่มรูปภาพ
        new ImageAdder(panel).start();

        try {
            InetAddress ip = InetAddress.getLocalHost();

            for (int i = 0; i < this.playerob.getPlayer() ; i++) {
                if (ip.getHostAddress().equals(this.playerob.getIP(i)))
                {
                    index = i;
                    System.out.println("Index gameplay : "+ index);
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("UnknowHost: "+ e);
        }

        System.out.println("skin : "+ this.playerob.getSkin(index));
        imageBg = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator +"Image"+ File.separator + "b.png");
        imagesGhost1 = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator +"Image"+ File.separator + "ghost1.png");
        imageCharacter = new Image[playerob.getPlayer()];

        for (int i = 0; i < playerob.getPlayer() ; i++) {
            imageCharacter[i] = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator +"Image"+ File.separator + String.valueOf(playerob.getSkin(i)) +".png");
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
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {}

                seconds = playerob.getSeconds();
                minutes = playerob.getMinutes();

                panel.repaint();
            }
        }
    }

    class CirclePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imageBg, 0, 0, getWidth(), getHeight(), this);
            for (int i = 0; i < playerob.getPlayer() ; i++) 
            {
                g.drawImage(imageCharacter[i], 100, 220 + (i *130), 150, 100, null);
            }

            timeString = String.format("%02d:%02d", minutes , seconds);        
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 25)); 
            g.drawString("Time Remaining: " + timeString + " seconds", 420, 30);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 25)); 

            // วาดรูปภาพทั้งหมดจาก Array
            if (playerob.hasPosition(index)) {
                for (int i = 0; i < playerob.getPlayer() ; i++) {
                    for (int k = 0; k < playerob.sizePosition(i); k++) {

                        if (null != playerob.getPosition(i, k)) {
                            g.drawImage(imagesGhost1, playerob.getPosition(i, k), playerob.getY(i), 85, 85, this);

                            // วาดคำที่อยู่บนรูปภาพจากอาเรย์ words
                            g.setColor(Color.WHITE);
                            g.setFont(new Font("Arial", Font.BOLD, 25)); 
                            g.drawString(playerob.getWord(i, k), playerob.getPosition(i, k) + 10, playerob.getY(i) - 10);
                        }
                    }
                }
            }
        }
    }
}
