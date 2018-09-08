import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends JFrame {
    private User signedUser;
    private PrepareGame preGame;
    private JButton bt;
    private JButton login;
    private JButton register;
    private JTextField mail;
    private JPasswordField password;
    private FileManagementSystem fms;
    private JFrame context;
    private ArrayList<User> highScore;


    Home() {
        super();
        fms = new FileManagementSystem();
        fms.ReadFileofUsers();
        highScore = new ArrayList<>();
        context = this;

        highScores();
        insertionSort(highScore);

        GUIInit();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        windowsClose();
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    private void GUIInit() {
        ImageIcon image = new ImageIcon("src/images/bg.png");
        ImageIcon logo = new ImageIcon("src/images/man.png");
        logo.setImage(getScaledImage(logo.getImage(), 70, 70));

        ImagePanel panel = new ImagePanel(image.getImage());
        getContentPane().add(panel);
        panel.setLayout(new FlowLayout());
        panel.setBorder(new EmptyBorder(90, 10, 10, 10));




        /* LEFT VE RIGHT panellerini içerir. */
        JPanel container = new JPanel();
        container.setBackground(new Color(0, 0, 0, 0));

        JPanel right = new JPanel(new GridBagLayout());
        right.setPreferredSize(new Dimension(916, 600));
        right.setBackground(new Color(255, 255, 255, 100));

        GridBagConstraints hc = new GridBagConstraints();

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(255, 255, 255, 180));
        infoPanel.setPreferredSize(new Dimension(300, 350));

        final String texts ="<h1>Minnoş</h1>İlk ve orta okul seviyesindeki herkese kodlama eğitimi vermeyi hedefleyen ücretsiz ve tamamen Türkçe bir platformdur.<br/><br/>Kod yazmaya erken yaşta başlamak isteyen tüm öğrenciler için eğlenceli ve öğretici bir platform olan Minnoş seviye seviye ilerlemekte ve her seviyede öğrenciye bilgiler sunup yapması gerekenleri anlatmaktadır.<br/><br/>Gebze Teknik Üniversitei Bilgisayar Mühendisliği öğrencileri tarafından geliştirilen proje güncellenmeye ve gelişmeye devam etmektedir.";
        final String html1 = "<html><body style='width: ";
        final String html2 = "px'>";
        infoPanel.add(new JLabel(html1 + "200" + html2 + texts));


        //infoPanel.add(a);


//        infoPanel.setSize(new Dimension(350,616));


        //high score
        JPanel highScorePanel = new JPanel(new GridBagLayout());
        highScorePanel.setBackground(new Color(255, 255, 255, 120));

//        highScorePanel.setBackground(new Color(255, 255, 255, 0));


        JLabel userLabel = new JLabel("Kullanıcı Adı");
        JLabel levelLabel = new JLabel("Kaldığı Seviye");
        JLabel pointLabel = new JLabel("Topladığı Puan");
        userLabel.setFont(userLabel.getFont().deriveFont(12f));
        userLabel.setForeground(Color.decode("#fe9e37"));
        levelLabel.setFont(userLabel.getFont().deriveFont(12f));
        levelLabel.setForeground(Color.decode("#fe9e37"));
        pointLabel.setFont(userLabel.getFont().deriveFont(12f));
        pointLabel.setForeground(Color.decode("#fe9e37"));

        userLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        levelLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        pointLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

        hc.ipadx = 50;
        hc.gridx = 1;
        hc.insets = new Insets(10, 0, 0, 0);
        highScorePanel.add(userLabel, hc);
        hc.gridx = 2;
        highScorePanel.add(levelLabel, hc);
        hc.ipadx = 0;
        hc.gridx = 3;
        highScorePanel.add(pointLabel, hc);

//        hc.insets = new Insets(20, 0, 0, 0);
        int i = 4;
        //add values to layout
        for (User user : highScore) {

            hc.ipadx = 0;
            hc.gridx = 0;
            if (i == 4)
                hc.gridy = 1;
            JLabel orderLabel = new JLabel(String.valueOf(i - 3));
//            orderLabel.setFont(userLabel.getFont().deriveFont(12f));
            orderLabel.setForeground(Color.DARK_GRAY);
            highScorePanel.add(orderLabel, hc);

            hc.ipadx = 50;
            String key = user.getUserName();
            String keyValue = key.substring(0, 1).toUpperCase() + key.substring(1);

            hc.gridx = 1;
            JLabel keyLabel = new JLabel(keyValue);
//            keyLabel.setFont(userLabel.getFont().deriveFont(18f));
            keyLabel.setForeground(Color.DARK_GRAY);
            highScorePanel.add(keyLabel, hc);

            hc.gridx = 2;
            JLabel lastLevelLabel = new JLabel(String.valueOf(user.getLevel()));
//            lastLevelLabel.setFont(userLabel.getFont().deriveFont(18f));
            lastLevelLabel.setForeground(Color.DARK_GRAY);
            highScorePanel.add(lastLevelLabel, hc);

            hc.ipadx = 0;
            hc.gridx = 3;
            Integer maxPoint = user.totalPoint();
            JLabel maxPointLabel = new JLabel(String.valueOf(maxPoint));
//            maxPointLabel.setFont(userLabel.getFont().deriveFont(18f));
            maxPointLabel.setForeground(Color.DARK_GRAY);
            highScorePanel.add(maxPointLabel, hc);

            hc.gridy = i++;
        }
        GridBagConstraints rightC = new GridBagConstraints();
        rightC.gridx = 0;
        rightC.gridy = 0;
        right.add(infoPanel, rightC);
        rightC.insets = new Insets(50, 50, 50, 50);
        rightC.gridx = 1;
        rightC.ipadx = 50;
        rightC.ipady = 50;
        right.add(highScorePanel, rightC);

        //high score end

        JPanel left = new JPanel(new GridBagLayout());
        left.setPreferredSize(new Dimension(350, 600));
        left.setBackground(new Color(255, 255, 255, 0));

        container.add(left);
        container.add(right);
        panel.add(container);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;

        /** LEFT İLE ALAKALI PANELLER*/
        JPanel topGrid = new JPanel(new FlowLayout());
        topGrid.setPreferredSize(new Dimension(300, 75));

        topGrid.add(new JLabel(logo));
        JLabel logoText = new JLabel("MİNNOŞ");
        logoText.setFont(logoText.getFont().deriveFont(18f));
        logoText.setForeground(Color.WHITE);
        topGrid.add(logoText);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(0, 0, 120, 0);
        topGrid.setBackground(new Color(255, 255, 255, 90));

        left.add(topGrid, gc);


        JPanel bottomGrid = new JPanel(new GridBagLayout());
        bottomGrid.setBackground(new Color(255, 255, 255, 98));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        mail = new JTextField("Mail Adresi", 20);
        mail.setVisible(false);
        mail.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (mail.getText().equals("Mail Adresi")) {
                    mail.setText("");
                    mail.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (mail.getText().isEmpty()) {
                    mail.setForeground(Color.GRAY);
                    mail.setText("Mail Adresi");
                }
            }
        });

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        mail.setHorizontalAlignment(SwingConstants.CENTER);
        mail.setPreferredSize(new Dimension(60, 40));
        bottomGrid.add(mail, c);

        password = new JPasswordField("Şifre", 20);
        password.setEchoChar((char) 0);
        password.setVisible(false);


        password.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (password.getText().equals("Şifre")) {
                    password.setText("");
                    password.setForeground(Color.BLACK);
                    password.setEchoChar('●');

                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (password.getText().isEmpty()) {
                    password.setForeground(Color.GRAY);
                    password.setText("Şifre");
                    password.setEchoChar((char) 0);

                }
            }
        });
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 0.0;
        password.setHorizontalAlignment(SwingConstants.CENTER);
        password.setPreferredSize(new Dimension(60, 40));
        bottomGrid.add(password, c);

        ButtonListener listener = new ButtonListener();

        login = new JButton("Giriş Yap");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 0, 2);
        login.addActionListener(listener);

        password.setHorizontalAlignment(SwingConstants.CENTER);
        password.setPreferredSize(new Dimension(60, 40));
        bottomGrid.add(login, c);

        register = new JButton("Kayıt Ol");
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(10, 2, 0, 0);
        register.addActionListener(listener);

        password.setHorizontalAlignment(SwingConstants.CENTER);
        password.setPreferredSize(new Dimension(60, 40));
        bottomGrid.add(register, c);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.ipady = 40;
        left.add(bottomGrid, gc);

        pack();
        setTitle("Minnoş");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1366, 790));
        setResizable(false);
        pack();
        setVisible(true);
        mail.setVisible(true);
        password.setVisible(true);

    }

    private int compareToUsers(String userName, String password) {
        for (int i = 0; i < fms.getUsers().size(); i++) {
            if (userName.equals(fms.getUsers().get(i).getEmail()) && password.equals(fms.getUsers().get(i).getPassword()))
                return i;
        }
        return -1;
    }

    /**
     * login islemi
     */
    private void Login() {
        int index = compareToUsers(mail.getText(), password.getText());
        if (index != -1) {
            PrepareGame pg = new PrepareGame(fms.getUsers().get(index), fms);
            pg.setVisible(true);
            setVisible(false);
        } else
            JOptionPane.showMessageDialog(null, "Sistemde Kayıtlı Değilsiniz. Lütfen Sisteme Kayıt Olunuz.");
    }

    /**
     * Kayit islemi
     */
    private void saveRegister() throws IOException {
        if (!(mail.getText().equals("Mail Adresi") && password.getText().equals("Şifre"))
                && !(mail.getText().equals("") && password.getText().equals(""))) {


            if (mail.getText().indexOf("@") == -1)
                JOptionPane.showMessageDialog(null, "LÜTFEN \"example@example.com\" GİBİ BİR MAİL ADRESİ İLE KAYIT OLUNUZ");
            else {
                int index = compareToUsers(mail.getText(), password.getText());
                if (index == -1) {
                    fms.getUsers().add(new User("", mail.getText(), password.getText(), "", new ArrayList<>()));
                    JOptionPane.showMessageDialog(null, "Kayıt İslemi Basarılı");
                    PrepareGame pg = new PrepareGame(fms.getUsers().get(fms.getUsers().size() - 1), fms);
                    fms.WriteFileofUsers();
                    pg.setVisible(true);
                    setVisible(false);
                } else
                    JOptionPane.showMessageDialog(null, "Sisteme Zaten Kayıtlısınız");
            }
        } else
            JOptionPane.showMessageDialog(null, "Lütfen Mail Adresi ve Şifre Alanını Doldurunuz");
    }


    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(login)) {
                Login();
            } else if (e.getSource().equals(register)) {
                try {
                    saveRegister();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void windowsClose() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                JFrame tmp = new JFrame();
                int res = JOptionPane.showConfirmDialog(getContentPane(),
                        "Minnoş'u terk mi ediyorsun?", "Gidiyor musun?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                switch (res) {
                    case JOptionPane.YES_OPTION: {
                        try {
                            fms.WriteFileofUsers();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        context.dispose();
                        System.exit(0);
                        break;
                    }
                    case JOptionPane.NO_OPTION: {
                        tmp.dispose();
                        break;
                    }
                }
            }
        });
    }

    private void highScores() {
        int size = fms.getUsers().size();
        for (int i = 0; i < size; ++i) {
            highScore.add(fms.getUsers().get(i));
        }
    }

    public void insertionSort(ArrayList<User> arr) {
        int n = arr.size();
        for (int i = 1; i < n; ++i) {
            int key = arr.get(i).totalPoint();
            User temp = arr.get(i);
            int j = i - 1;
            while (j >= 0 && arr.get(j).totalPoint() < key) {
                arr.set(j + 1, arr.get(j));
                j = j - 1;
            }
            arr.set(j + 1, temp);

        }
    }
}


