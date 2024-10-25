import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class run_ghost extends JFrame implements KeyListener {
    int[] ghostX; // Array สำหรับเก็บตำแหน่ง x ของรูปภาพ
    int[] ghostY; // Array สำหรับเก็บตำแหน่ง y ของรูปภาพ

    int ghostCount = 0; // นับจำนวนรูปภาพใน Array
    int ghost1 = 0;
    int maxGhost = 100; // จำนวนรูปภาพสูงสุดที่รองรับ

    int[] xSpeed; // ความเร็วในแนวนอนของแต่ละรูปภาพ

    boolean running = true; // ตัวแปรสำหรับควบคุมการทำงานของเธรด
    Image[] imagesGhost1; // อาเรย์สำหรับเก็บรูปภาพ
    String[] words; // อาเรย์สำหรับเก็บคำที่ตรงกับรูปภาพ
    int remainingTime = 1000;
    Image imageGun;
    Image imageBg;
    Image imageChaerecter;
    JTextField check_text;
    String data = "";
    boolean bang = false;
    int J;

    String[] shortVocabulary = { "growth", "market", "energy", "safety", "health",
            "impact", "change", "rights", "supply", "demand",
            "travel", "export", "import", "survey", "status",
            "profit", "credit", "carbon", "crisis", "policy",
            "trade", "income", "budget", "people", "global",
            "labour", "public", "future", "border", "manage",
            "report", "result", "access", "nation", "sector",
            "support", "output", "target", "reform", "survey",
            "profit", "export", "import", "credit", "debate",
            "review", "access", "policy", "growth", "carbon",
            "ronnakit", "teeranai", "nattapong", "jetsada", "peerapong"
    };

    public run_ghost() {
        setTitle("");
        setSize(1200, 800); // กำหนดขนาดหน้าต่าง
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        check_text = new JTextField();
        check_text.setSize(1, 1);
        check_text.setLocation(0, 0);
        check_text.addKeyListener(this);

        // สร้าง Array ขนาดคงที่สำหรับตำแหน่ง x และ y
        ghostX = new int[maxGhost];
        ghostY = new int[maxGhost];
        xSpeed = new int[maxGhost]; // สร้าง Array เก็บความเร็วแนวนอน
        words = new String[maxGhost]; // สร้างอาเรย์สำหรับเก็บคำ
        imageBg = Toolkit.getDefaultToolkit()
                .getImage(System.getProperty("user.dir") + File.separator + "Image" + File.separator + "b.png");
        imageGun = Toolkit.getDefaultToolkit()
                .getImage(System.getProperty("user.dir") + File.separator + "Image" + File.separator + "6.png");

        // สร้างอาเรย์สำหรับเก็บรูปภาพ
        imagesGhost1 = new Image[maxGhost]; // ประกาศอาเรย์สำหรับเก็บรูปภาพ

        // โหลดรูปภาพ (เปลี่ยนเส้นทางเป็นที่อยู่จริงของรูปภาพ)
        for (int i = 0; i < maxGhost; i++) {
            try {
                imagesGhost1[i] = Toolkit.getDefaultToolkit()
                        .getImage(System.getProperty("user.dir") + File.separator + "Image" + File.separator
                                + "ghost1.png"); // ใช้ชื่อไฟล์ที่แตกต่างกันสำหรับแต่ละภาพ
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CirclePanel panel = new CirclePanel();
        panel.setBackground(Color.BLACK);
        add(check_text);
        add(panel);

        // เรียกใช้เธรดสำหรับเพิ่มรูปภาพ
        new ImageAdder().start();

        // เรียกใช้เธรดสำหรับอัปเดตตำแหน่งรูปภาพ
        new ImageMover(panel).start();

        // เรียกใช้เธรดสำหรับจับเวลานับถอยหลัง
        new TimerThread().start();

    }

    // สร้างคลาส Thread สำหรับเพิ่มรูปภาพ
    class ImageAdder extends Thread {
        @Override
        public void run() {
            while (running) {
                if (ghostCount < maxGhost) {
                    // เริ่มจากตำแหน่ง x = getWidth() (ด้านขวา) และตำแหน่ง y แบบสุ่มในหน้าต่าง
                    ghostX[ghostCount] = getWidth(); // เริ่มจากด้านขวาของหน้าต่าง
                    ghostY[ghostCount] = 220;
                    xSpeed[ghostCount] = -1; // กำหนดความเร็วให้เคลื่อนไปทางซ้าย

                    // เลือกคำแบบสุ่มจาก shortVocabulary และเก็บในอาเรย์ words
                    words[ghostCount] = shortVocabulary[(int) (Math.random() * shortVocabulary.length)];

                    ghostCount++; // เพิ่มจำนวนรูปภาพ
                }

                try {
                    Thread.sleep(10000); // รอ 10 วินาที
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // สร้างคลาส Thread สำหรับจับเวลานับถอยหลัง
    class TimerThread extends Thread {
        @Override
        public void run() {
            while (remainingTime > 0 && running) {
                try {
                    Thread.sleep(1000); // รอ 1 วินาที
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                remainingTime--; // ลดเวลาที่เหลือ
            }

            if (remainingTime == 0) {
                for (int i = 0; i < ghostCount; i++) {
                    imagesGhost1[i] = null;
                    words[i] = null;
                }
            }
            running = false; // หยุดการทำงานของโปรแกรมเมื่อเวลาหมด
            repaint();
        }
    }

    // สร้างคลาส Thread สำหรับอัปเดตตำแหน่งรูปภาพ
    class ImageMover extends Thread {
        private CirclePanel panel;

        public ImageMover(CirclePanel panel) {
            this.panel = panel;
        }

        @Override
        public void run() {
            while (running) {
                // อัปเดตตำแหน่งของรูปภาพทุกลูก
                for (int i = 0; i < ghostCount; i++) {
                    ghostX[i] += xSpeed[i]; // เคลื่อนที่รูปภาพในแนวนอน

                    // ตรวจสอบว่าชนขอบหน้าต่างด้านซ้ายหรือไม่
                    if (imagesGhost1[i] != null) {
                        if (ghostX[i] < 250) {
                            imagesGhost1[i] = null;
                            words[i] = null;
                            ghost1--;
                        }
                    }

                    if (imagesGhost1[i] != null) { // การยิง
                        if (data.equals(words[i])) {
                            imagesGhost1[i] = null;
                            words[i] = null;
                            data = "";
                            J = i + 1;
                            check_text.setText("");
                            ghost1++;
                            System.out.println("banggg!!!");
                        }
                    }
                }
                panel.repaint(); // วาดรูปภาพใหม่

                // รอ 30 มิลลิวินาทีก่อนที่จะอัปเดตตำแหน่งอีกครั้ง
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // สร้าง JPanel สำหรับการวาดรูปภาพ
    class CirclePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imageBg, 0, 0, getWidth(), getHeight(), this);
            g.drawImage(imageGun, 100, 220, 150, 100, this);
            // วาดรูปภาพทั้งหมดจาก Array
            for (int i = 0; i < ghostCount; i++) {
                g.drawImage(imagesGhost1[i], ghostX[i], ghostY[i], 85, 85, null);

                // วาดคำที่อยู่บนรูปภาพจากอาเรย์ words
                if (words[i] != null) {
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 25));
                    g.drawString(words[i], ghostX[i] + 10, ghostY[i] - 10);
                }
                int minutes = remainingTime / 60;
                int seconds = remainingTime % 60;
                String timeString = String.format("%02d:%02d", minutes, seconds);

                // แสดงเวลาที่เหลือ
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 25));
                g.drawString("Time Remaining: " + timeString + " seconds", 420, 30);

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 25));
                g.drawString("ghost1: " + ghost1 + " Count", 800, 80);

                if (bang == true) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.RED);
                    g2d.setStroke(new BasicStroke(10.0f)); // ความหนา 5 พิกเซล
                    g2d.setColor(Color.RED);
                    g2d.drawLine(260, 260, ghostX[J], 260);
                    try {
                        Thread.sleep(30);
                        bang = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 25));
                g.drawString(data, 110, 210);
            }
        }
    }

    public static void main(String[] args) {
        // เพื่อให้การทำงานของ Swing GUI เป็นไปอย่างราบรื่น
        SwingUtilities.invokeLater(() -> new run_ghost().setVisible(true));
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