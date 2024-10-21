import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class SettingPanel extends JPanel {
    Image background = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator + "picture" + File.separator + "background.png");
    Image container = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator + "picture" + File.separator + "Backdrop.png");
    Image imageArrow = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator + "picture" + File.separator + "Arrow.png");
    Icon imgplay = new ImageIcon(System.getProperty("user.dir") + File.separator + "picture" + File.separator + "play.png");
    Icon imgback = new ImageIcon(System.getProperty("user.dir") + File.separator + "picture" + File.separator + "back.png");
    JPanel containgun = new JPanel();
    Icon [] skin = new Icon[4];
    JButton [] gun = new JButton[4];
    JButton next = new JButton();
    JButton back = new JButton(); 
    JTextField inputIP = new JTextField();
    JTextField inputName = new JTextField();
    JLabel textReady = new JLabel("Ready : (3/4)");

    public SettingPanel() {
        this.setSize(1200, 800);
        this.setLayout(null);

        containgun.setSize(600, 60);
        containgun.setLayout(new GridLayout(1, 4, 10, 0));
        containgun.setLocation(300, 425);
        containgun.setOpaque(false);  // ทำให้ containgun โปร่งใส

        next.setBounds(800, 540, 140, 50);
        next.setIcon(imgplay);
        next.setOpaque(false);  // ทำให้ปุ่มโปร่งใส
        next.setContentAreaFilled(false);  // ลบสีพื้นหลังของปุ่ม
        next.setBorderPainted(false);  // ไม่แสดงขอบของปุ่ม

        back.setBounds(250, 540, 140, 50);
        back.setIcon(imgback);
        back.setOpaque(false);  // ทำให้ปุ่มโปร่งใส
        back.setContentAreaFilled(false);  // ลบสีพื้นหลังของปุ่ม
        back.setBorderPainted(false);  // ไม่แสดงขอบของปุ่ม

        inputIP.setBounds(410, 290, 200, 30);
        inputName.setBounds(410, 370, 200, 30);

        textReady.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textReady.setBounds(820, 505, 100, 30);

        for (int i = 0; i < gun.length ; i++) {
            skin[i] = new ImageIcon(System.getProperty("user.dir") + File.separator + "picture" + File.separator + String.valueOf(i+1) +".png");

            gun[i] = new JButton();
            gun[i].setIcon(skin[i]);
            gun[i].setOpaque(false);  // ทำให้ปุ่มโปร่งใส
            gun[i].setContentAreaFilled(false);  // ลบสีพื้นหลังของปุ่ม
            containgun.add(gun[i]);
        }

        this.add(containgun);
        this.add(next);
        this.add(back);
        this.add(inputIP);
        this.add(inputName);
        this.add(textReady);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(container, 250, 100, 700, 500, this);
        g.drawImage(imageArrow, 350, 200, 50, 50, this);
        g.drawImage(imageArrow, 350, 280, 50, 50, this);
        g.drawImage(imageArrow, 350, 360, 50, 50, this);

        g.setFont(new Font("Tahoma", Font.BOLD, 40));
        g.setColor(Color.BLACK);
        g.drawString("SETTING", 500, 150);

        g.setFont(new Font("Tahoma", Font.BOLD, 15));
        g.drawString("Player Position", 425, 200);
        g.drawString("Enter IP Server", 425, 280);
        g.drawString("Enter Player name", 425, 360);
    }
}

class Setting extends JFrame {
    SettingPanel panel = new SettingPanel();

    public Setting() {
        this.setSize(1200, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(panel);
    }

    public static void main(String[] args) {
        Setting frame = new Setting();
        frame.setVisible(true);
    }
}
