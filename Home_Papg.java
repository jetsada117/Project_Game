
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Home_Papg extends JFrame implements ActionListener{
    
    private Image imageName = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator +"Image"+ File.separator+ "I_name.png");
    private Image imageBg = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator +"Image"+ File.separator+ "Screenshot 2024-10-08 211105.png");

    private JButton btnStart = new JButton();
    private JButton btnSetting = new JButton();
    private JButton btnExit = new JButton();

    private JPanel panel = new JPanel(){
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imageBg, 0, 0, getWidth(), getHeight(), this);
            g.drawImage(imageName, 280, 100, imageName.getWidth(this), imageName.getHeight(this), this);
        }
    };

    public Home_Papg(){

        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel.setSize(1200, 800);
        panel.setLayout(null); 
        
        btn();

        panel.add(btnStart);
        // panel.add(btnSetting);
        panel.add(btnExit);

        add(panel);

        btnStart.addActionListener(this);
        btnSetting.addActionListener(this);
        btnExit.addActionListener(this);
    }
    
    void btn(){
                Icon imageStart = new ImageIcon(System.getProperty("user.dir") + File.separator +"Image"+ File.separator+ "I_start.png");
                //Icon imageSetting = new ImageIcon(System.getProperty("user.dir") + File.separator +"Image"+ File.separator+ "I_setting.png");
                Icon imageExit = new ImageIcon(System.getProperty("user.dir") + File.separator +"Image"+ File.separator+ "I_exit.png");

                btnStart.setBounds(425, 300, imageStart.getIconWidth(), imageStart.getIconHeight());
                //btnSetting.setBounds(425, 425, imageSetting.getIconWidth(), imageSetting.getIconHeight());
                btnExit.setBounds(425, 550, imageExit.getIconWidth(), imageExit.getIconHeight());
        
                btnStart.setIcon(imageStart);
                //btnSetting.setIcon(imageSetting);
                btnExit.setIcon(imageExit);
        
                btnStart.setContentAreaFilled(false);
                btnStart.setFocusPainted(false);
        
                btnSetting.setContentAreaFilled(false);
                btnSetting.setFocusPainted(false);
        
                btnExit.setContentAreaFilled(false);
                btnExit.setFocusPainted(false);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnStart) {
            System.out.println("Strat");
            run_ghost run_ghost = new run_ghost();
            run_ghost.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            run_ghost.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e){
                        setVisible(true);
                    }
            });
            
            run_ghost.setResizable(false);   
            run_ghost.setLocationRelativeTo(null);
            run_ghost.setVisible(true);

            setVisible(false);
        }
        // else if (e.getSource() == btnSetting) {
        //     System.out.println("Setting");
        // }
        
        else if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        Home_Papg home_Papg = new Home_Papg();
        home_Papg.setVisible(true);
    }
}
