import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class run_ghost extends JFrame {
    int[] ghostX;  // Array สำหรับเก็บตำแหน่ง x ของรูปภาพ
    int[] ghostY;  // Array สำหรับเก็บตำแหน่ง y ของรูปภาพ
    int ghostCount = 0;  // นับจำนวนรูปภาพใน Array
    int ghost1 = 0;
    int maxGhost = 100;  // จำนวนรูปภาพสูงสุดที่รองรับ
    int[] xSpeed;  // ความเร็วในแนวนอนของแต่ละรูปภาพ
    boolean running = false;  // ตัวแปรสำหรับควบคุมการทำงานของเธรด
    Image[] imagesGhost1;  // อาเรย์สำหรับเก็บรูปภาพ
    String[] words;  // อาเรย์สำหรับเก็บคำที่ตรงกับรูปภาพ
    String timeString;
    Image imageGun;
    Image imageBg;
    PlayerAll playerob;
    int minutes = 5;
    int seconds;

    String[] shortVocabulary = 
    {   "growth", "market", "energy", "safety", "health", 
        "impact", "change", "rights", "supply", "demand", 
        "travel", "export", "import", "survey", "status", 
        "profit", "credit", "carbon", "crisis", "policy", 
        "trade", "income", "budget", "people", "global", 
        "labour", "public", "future", "border", "manage", 
        "report", "result", "access", "nation", "sector", 
        "supply", "output", "target", "reform", "survey", 
        "profit", "export", "import", "credit", "debate", 
        "review", "access", "policy", "growth", "carbon",
        "ronnakit", "teeranai", "nattapong", "jetsada", "peerapong"
    };

    public run_ghost(PlayerAll playerob) {
        this.playerob = playerob;

        setTitle("");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // สร้าง Array ขนาดคงที่สำหรับตำแหน่ง x และ y
        ghostX = new int[maxGhost];
        ghostY = new int[maxGhost];
        xSpeed = new int[maxGhost];  // สร้าง Array เก็บความเร็วแนวนอน
        words = new String[maxGhost];  // สร้างอาเรย์สำหรับเก็บคำ
        imageBg = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator +"Image"+ File.separator + "b.png");
        imageGun = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator +"Image"+ File.separator + "6.png");

        // สร้างอาเรย์สำหรับเก็บรูปภาพ
        imagesGhost1 = new Image[maxGhost];  // ประกาศอาเรย์สำหรับเก็บรูปภาพ

        // โหลดรูปภาพ (เปลี่ยนเส้นทางเป็นที่อยู่จริงของรูปภาพ)
        for (int i = 0; i < maxGhost; i++) {
            try {
                imagesGhost1[i] = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator +"Image"+ File.separator + "ghost1.png"); // ใช้ชื่อไฟล์ที่แตกต่างกันสำหรับแต่ละภาพ
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CirclePanel panel = new CirclePanel();
        panel.setBackground(Color.BLACK);
        add(panel);

        // เรียกใช้เธรดสำหรับเพิ่มรูปภาพ
        new ImageAdder(panel).start();

        // เรียกใช้เธรดสำหรับอัปเดตตำแหน่งรูปภาพ
        new ImageMover(panel).start();
    }

    // สร้างคลาส Thread สำหรับเพิ่มรูปภาพ
    class ImageAdder extends Thread {
        private CirclePanel panel;

        public ImageAdder(CirclePanel panel) {
            this.panel = panel;
        }

        @Override
        public void run() {
            while (true) {
                System.out.println("isStart[run] : "+ playerob.isIsStart());

                if (playerob.isIsStart()) {
                    minutes = playerob.getMinutes();
                    seconds = playerob.getSeconds();
                    System.out.println("Minutes : "+ minutes +", Seconds : "+ seconds);
                    System.out.println("boolean : "+ (seconds % 10 == 0) +" , boolean : "+ (minutes < 5));
        
                    if ((seconds % 10 == 0) && (minutes < 5)) {
                        System.out.println("Ghost Time!");
    
                        // เริ่มจากตำแหน่ง x = getWidth() (ด้านขวา) และตำแหน่ง y แบบสุ่มในหน้าต่าง
                        ghostX[ghostCount] = getWidth();  // เริ่มจากด้านขวาของหน้าต่าง
                        ghostY[ghostCount] = 220;
                        xSpeed[ghostCount] = -1;  // กำหนดความเร็วให้เคลื่อนไปทางซ้าย
                            
                        // เลือกคำแบบสุ่มจาก shortVocabulary และเก็บในอาเรย์ words
                        words[ghostCount] = shortVocabulary[(int) (Math.random() * shortVocabulary.length)];
                        
                        ghostCount++;  // เพิ่มจำนวนรูปภาพ

                        System.out.println("Ghost count : "+ ghostCount);
                    }
    
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                
                    panel.repaint();
                }
            }
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
            while (true) {
                if (playerob.isIsStart()) {
                    for (int i = 0; i < ghostCount; i++) {
                        ghostX[i] += xSpeed[i];  // เคลื่อนที่รูปภาพในแนวนอน

                        // ตรวจสอบว่าชนขอบหน้าต่างด้านซ้ายหรือไม่
                        if (imagesGhost1[i] != null) {
                            if (ghostX[i] < 250) {
                                imagesGhost1[i] = null;   
                                words[i] = null; 
                                ghost1++;
                            }
                        }
                    }

                    panel.repaint();  // วาดรูปภาพใหม่

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
                else {
                    System.out.println("playerob.isIsStart() is false");

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
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

            timeString = String.format("%02d:%02d", minutes , seconds);        
            
            // แสดงเวลาที่เหลือ
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 25)); 
            g.drawString("Time Remaining: " + timeString + " seconds", 420, 30);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 25)); 
            g.drawString("ghost1: " + ghost1 + " Count", 800, 80);
            
            if (minutes <= 0 && seconds <= 0) {
                for (int k = 0; k < ghostCount; k++) {
                    imagesGhost1[k] = null;  
                    words[k] = null;  
                }
            }

            // วาดรูปภาพทั้งหมดจาก Array
            for (int i = 0; i < ghostCount; i++) {
                g.drawImage(imagesGhost1[i], ghostX[i], ghostY[i], 85, 85, null);

                // วาดคำที่อยู่บนรูปภาพจากอาเรย์ words
                if (words[i] != null) {
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 25)); 
                    g.drawString(words[i], ghostX[i] + 10, ghostY[i] - 10);
                }
            }
        }
    }
}
