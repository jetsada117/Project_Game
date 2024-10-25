import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Home extends JFrame implements ActionListener {
    private final JButton btnStart = new JButton();
    private final JButton btnExit = new JButton();

    private Image imageName = Toolkit.getDefaultToolkit()
            .getImage(System.getProperty("user.dir") + File.separator + "Image" + File.separator + "I_name.png");
    private Image imageBg = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator
            + "Image" + File.separator + "Screenshot 2024-10-08 211105.png");
    private Image image = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator
            + "Image" + File.separator + "XXX.png");

    private final JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imageBg, 0, 0, getWidth(), getHeight(), this);
            g.drawImage(imageName, 260, 100, imageName.getWidth(this) + 50, imageName.getHeight(this) + 50, this);
            g.drawImage(image, 630, 350, image.getWidth(this), image.getHeight(this), this);
        }
    };

    public Home() {
        setUndecorated(true);
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel.setSize(1200, 800);
        panel.setLayout(null);

        btn();
        playSound sound = new playSound();
        sound.start();

        panel.add(btnStart);
        panel.add(btnExit);

        add(panel);

        btnStart.addActionListener(this);
        btnExit.addActionListener(this);
    }

    void btn() {
        Icon imageStart = new ImageIcon(
                System.getProperty("user.dir") + File.separator + "Image" + File.separator + "I_start.png");
        Icon imageExit = new ImageIcon(
                System.getProperty("user.dir") + File.separator + "Image" + File.separator + "I_exit.png");

        btnStart.setBounds(425, 320, imageStart.getIconWidth(), imageStart.getIconHeight());
        btnExit.setBounds(425, 450, imageExit.getIconWidth(), imageExit.getIconHeight());

        btnStart.setIcon(imageStart);
        btnExit.setIcon(imageExit);

        btnStart.setContentAreaFilled(false);
        btnStart.setFocusPainted(false);

        btnExit.setContentAreaFilled(false);
        btnExit.setFocusPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnStart) {
            System.out.println("Start");

            Setting setting = new Setting(this);
            setting.setVisible(true);

            this.setVisible(false);
        } else if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        Home home = new Home();
        home.setVisible(true);
    }
}

class playSound extends Thread {
    @Override
    public void run() {
        try {
            File wavFile = new File(System.getProperty("user.dir") + File.separator + "ghost_Audio_2.0.wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(wavFile);
            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            while (clip.isActive()) {
                Thread.sleep(1000);
            }

        } catch (IOException | InterruptedException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println("sound : " + e);
        }
    }
}