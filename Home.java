import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Home extends JFrame implements ActionListener{
    private final JButton btnStart = new JButton();
    private final JButton btnExit = new JButton();

    private Image imageName = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator +"Image"+ File.separator+ "I_name.png");
    private Image imageBg = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator +"Image"+ File.separator+ "Screenshot 2024-10-08 211105.png");

    private final JPanel panel = new JPanel(){
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imageBg, 0, 0, getWidth(), getHeight(), this);
            g.drawImage(imageName, 280, 100, imageName.getWidth(this), imageName.getHeight(this), this);
        }
    };

    public Home(){

        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel.setSize(1200, 800);
        panel.setLayout(null); 
        
        btn();

        panel.add(btnStart);
        panel.add(btnExit);

        add(panel);

        btnStart.addActionListener(this);
        btnExit.addActionListener(this);
    }
    
    void btn(){
        Icon imageStart = new ImageIcon(System.getProperty("user.dir") + File.separator +"Image"+ File.separator+ "I_start.png");
        Icon imageExit = new ImageIcon(System.getProperty("user.dir") + File.separator +"Image"+ File.separator+ "I_exit.png");

        btnStart.setBounds(425, 300, imageStart.getIconWidth(), imageStart.getIconHeight());
        btnExit.setBounds(425, 500, imageExit.getIconWidth(), imageExit.getIconHeight());

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

            Setting setting = new Setting();
            setting.setVisible(true);

            this.setVisible(false);
        }
        else if (e.getSource() == btnExit) 
        {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        Home home = new Home();
        home.setVisible(true);
    }
}
