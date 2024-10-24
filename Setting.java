import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

class SettingPanel extends JPanel implements ActionListener {
    Image background = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator + "image" + File.separator + "background.png");
    Image container = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator + "image" + File.separator + "bg2.png");
    Image imageArrow = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator + "image" + File.separator + "Arrow.png");
    Icon imgplay = new ImageIcon(System.getProperty("user.dir") + File.separator + "image" + File.separator + "play.png");
    Icon imgback = new ImageIcon(System.getProperty("user.dir") + File.separator + "image" + File.separator + "back.png");
    Icon imgconnect = new ImageIcon(System.getProperty("user.dir") + File.separator + "image" + File.separator + "connect.png");
    JPanel containgun = new JPanel();
    JPanel textbox = new JPanel();
    Icon [] skin = new Icon[4];
    JButton [] gun = new JButton[4];
    JButton connect = new JButton();
    JButton next = new JButton();
    JButton back = new JButton(); 
    JTextField inputIP = new JTextField();
    JTextField inputName = new JTextField();
    JLabel textNumber = new JLabel("Player :");
    JLabel textReady = new JLabel("Ready : (0/0)");
    JLabel textCount = new JLabel();
    PlayerObject player = new PlayerObject();
    PlayerAll playerob = new PlayerAll();
    run_ghost playgame;
    Setting setting;

    public SettingPanel(Setting setting) {
        this.setting = setting; // รับอ้างอิงไปยัง Setting

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

        connect.setBounds(650, 540, 140, 50);
        connect.setIcon(imgconnect);
        connect.setOpaque(false);  // ทำให้ปุ่มโปร่งใส
        connect.setContentAreaFilled(false);  // ลบสีพื้นหลังของปุ่ม
        connect.setBorderPainted(false);  // ไม่แสดงขอบของปุ่ม

        inputIP.setBounds(410, 290, 200, 30);
        inputName.setBounds(410, 370, 200, 30);

        textReady.setForeground(Color.ORANGE);
        textReady.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textReady.setBounds(820, 505, 100, 30);

        textNumber.setForeground(Color.black);
        textNumber.setFont(new Font("Tahoma", Font.BOLD, 15));
        textNumber.setHorizontalAlignment(SwingConstants.LEFT);

        textbox.setBounds(410, 210, 200, 30);
        textbox.setLayout(new FlowLayout(FlowLayout.LEFT));
        textbox.add(textNumber);

        textCount.setForeground(Color.ORANGE);
        textCount.setFont(new Font("Tahoma", Font.BOLD, 40));
        textCount.setBounds(820, 150, 100, 100);

        this.add(containgun);
        this.add(next);
        this.add(back);
        this.add(inputIP);
        this.add(inputName);
        this.add(textReady);
        this.add(textbox);
        this.add(textCount);
        this.add(connect);

        for (int i = 0; i < gun.length ; i++) {
            skin[i] = new ImageIcon(System.getProperty("user.dir") + File.separator + "image" + File.separator + String.valueOf(i+1) +".png");

            gun[i] = new JButton();
            gun[i].setIcon(skin[i]);
            gun[i].setOpaque(false);  // ทำให้ปุ่มโปร่งใส
            gun[i].setContentAreaFilled(false);  // ลบสีพื้นหลังของปุ่ม
            containgun.add(gun[i]);

            gun[i].addActionListener(this);
        }

        next.addActionListener(this);
        back.addActionListener(this);
        connect.addActionListener(this);

        this.setting.add(this);

        ClientThread thread = new ClientThread(this, playgame, this.setting, playerob);
        thread.start();
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
        g.setColor(Color.ORANGE);
        g.drawString("SETTING", 500, 150);

        g.setFont(new Font("Tahoma", Font.BOLD, 15));
        g.drawString("Player Position", 425, 200);
        g.drawString("Enter IP Server", 425, 280);
        g.drawString("Enter Player name", 425, 360);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            System.out.println("back");
        }
        else if (e.getSource() == connect) {
            System.out.println("connect");
            connect();
        }
        else if (e.getSource() == next) {
            System.out.println("next");
            ready();
        }
        else if (e.getSource() == gun[0])
        {
            player.setSkin(5);
            gun[0].setEnabled(false);

            for (int i = 0; i < gun.length; i++)
            {
                if (i == 0) continue;

                gun[i].setEnabled(true);
            }
        }
        else if (e.getSource() == gun[1])
        {
            player.setSkin(6);
            gun[1].setEnabled(false);

            for (int i = 0; i < gun.length; i++)
            {
                if (i == 1) continue;

                gun[i].setEnabled(true);
            }
        }
        else if (e.getSource() == gun[2])
        {
            player.setSkin(7);
            gun[2].setEnabled(false);

            for (int i = 0; i < gun.length; i++)
            {
                if (i == 2) continue;

                gun[i].setEnabled(true);
            }
        }
        else if (e.getSource() == gun[3])
        {
            player.setSkin(8);
            gun[3].setEnabled(false);

            for (int i = 0; i < gun.length; i++)
            {
                if (i == 3) continue;

                gun[i].setEnabled(true);
            }
        }
    }

    void connect() {
        // สร้าง socket เพื่อส่งค่า
        try (Socket socket = new Socket(inputIP.getText(), 50101)) {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            
            // set ค่าลง object
            player.setName(inputName.getText());

            // ส่งค่า object ออกไป
            objectOutput.writeObject(player);

            connect.setEnabled(false);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    void ready() {
        // สร้าง socket เพื่อส่งค่า
        try (Socket socket = new Socket(inputIP.getText(), 50101)) {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            
            // ถ้ากดเตรียมพร้อมให้ set true ลง ready
            player.setReady(true);

            // ส่งค่า object ออกไป
            objectOutput.writeObject(player);

        } catch (IOException e) {
            System.out.println(e);
        }
        
    }
}

class ClientThread extends Thread {
    SettingPanel client;
    run_ghost playgame;
    Setting setting;
    PlayerAll playerob;
    boolean isPlaying = false;
    int index;
    int player;
    int countReady;

    public ClientThread(SettingPanel client , run_ghost playgame, Setting setting, PlayerAll playerob) {
        this.client = client;
        this.playgame = playgame;
        this.setting = setting;
        this.playerob = playerob;
    }

    @Override
    public void run() {
        ServerSocket serverSock;
        try {
            serverSock = new ServerSocket(5);

            while (true) {
                // รับค่าผ่าน  socket 
                try (Socket socket = serverSock.accept();
                    InputStream input = socket.getInputStream();
                    ObjectInputStream objectInput = new ObjectInputStream(input)) {

                    // อ่าน object
                    Object receivedObject = objectInput.readObject();

                // เช็คชนิดข้อมูลว่าเป็นชนิดข้อมูล SeverObject หรือไม่
                if (receivedObject instanceof ServerObject Serverob) {
                    index = Serverob.getIndex();
                    player = Serverob.getPlayer();
                    playerob.setPlayer(player);
                    playerob.setIndex(index);
                    playerob.setSkin(Serverob.getSkin(index), index);

                    for (int i = 0; i < player; i++) {
                        playerob.setReady(Serverob.isReady(i), i);
                    }

                    // ถ้ากดเตรียมพร้อมทุกคนแล้วให้นับเลข แล้วแสดงเวลา
                    if (allPlayersReady()) {

                        if (Serverob.getCount() > 0) 
                        {
                            client.textCount.setText("" + String.valueOf(Serverob.getCount()));
                        } 
                        else
                        {
                            playerob.setMinutes(Serverob.getMinutes());
                            playerob.setSeconds(Serverob.getSeconds());
                        }
                    } 
                    else 
                    {
                        switch (Serverob.getIndex()) {
                            case 0:
                                System.out.println(player);
                                client.textNumber.setText("Player : 1st Player");
                                break;
                            case 1:
                                System.out.println(player);
                                client.textNumber.setText("Player : 2nd Player");
                                break;
                            case 2:
                                System.out.println(player);
                                client.textNumber.setText("Player : 3rd Player");
                                break;
                            case 3:
                                System.out.println(player);
                                client.textNumber.setText("Player : 4th Player");
                                break;
                            default:
                                break;
                        }
                    }

                    client.textReady.setText("Ready : (" + countReady + "/" + player +")");

                    if(allPlayersReady() && !isPlaying && Serverob.getCount() <= 0) 
                    {
                        playgame = new run_ghost(playerob);

                        // เปิดเฟรม run_ghost
                        playgame.setVisible(true);                        
                        playerob.setIsStart(true);

                        System.out.println("isStart[setting] : "+ playerob.isIsStart());
                        
                        // ปิดเฟรม Setting
                        setting.dispose(); // ปิด Setting frame

                        isPlaying = true;
                    }
                }

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } 
    }

    boolean allPlayersReady() {
        // เช็คสถานะ ready ของผู้เล่นทั้งหมด (แก้ไขให้ตรงกับโครงสร้างของคุณ)
        // นี่เป็นตัวอย่างการเช็คผู้เล่น 4 คนว่าพร้อมหรือไม่
        int readyPlayers = 0;
        for (int i = 0; i < 4; i++) {
            if (playerob.isReady(i)) {
                readyPlayers++;
            }
        }
        
        countReady = readyPlayers;

        // ถ้าผู้เล่นทั้งหมดพร้อม return true
        return readyPlayers == player;
    }
}

class Setting extends JFrame {
    public Setting() {
        this.setSize(1200, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(new SettingPanel(this)); // ส่ง Setting ให้ SettingPanel
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Setting frame = new Setting();
    }
}
