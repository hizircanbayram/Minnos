import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;


public class PrepareGame extends JFrame {
    private int level;
    private ArrayList<ArrayList<Cell>> grid;
    private User user;
    private JLabel[] levelButtons;
    private int maxLevel;
    private JFrame context;
    private Font levelFont;
    private PopupScreen afterLogin;
    private Font defaultFont;
    private FileManagementSystem fms;
    private JLabel nameValue;
    JLabel logoLabel;

    PrepareGame(User user, FileManagementSystem fms) {
        super();
        this.user = user;
        this.fms = fms;
        grid = new ArrayList<>();
        for (int i = 0; i < 7; ++i)
            grid.add(new ArrayList<>());
        maxLevel = 15;
        context = this;
        try {
            levelFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/levelFont.ttf"));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        GUIInit();
        if(user.getUserName().length()<3)
            callPopup();
    }

    private void callPopup() {

        ImageIcon man1 = new ImageIcon("src/images/man.png");
        ImageIcon man2 = new ImageIcon("src/images/man1.png");
        ImageIcon man3 = new ImageIcon("src/images/man2.png");
        ImageIcon man4 = new ImageIcon("src/images/man3.png");
        JPanel container = new JPanel(new GridBagLayout());

        JLabel userName = new JLabel("Kullanıcı Adı");
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 10;
        c.ipadx = 5;
        container.add(userName, c);

        JTextField userNameVal = new JTextField(" ", 10);
        userNameVal.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridx = 0;
        c.gridy = 1;
        container.add(userNameVal, c);


        JLabel selectChar = new JLabel("Karakter Seçiniz");
        c.gridx = 0;
        c.gridy = 2;
        container.add(selectChar, c);


        c.gridwidth = 1;



        URL url = PrepareGame.class.getResource("images");

        String html = "<html><body><img src='" + url.toString()+"/man.png'>";
        JRadioButton r1 = new JRadioButton(html);
        html = "<html><body><img src='" + url.toString()+"/man1.png'>";
        JRadioButton r2 = new JRadioButton(html);
        html = "<html><body><img src='" + url.toString()+"/man2.png'>";
        JRadioButton r3 = new JRadioButton(html);
        html = "<html><body><img src='" + url.toString()+"/man3.png'>";
        JRadioButton r4 = new JRadioButton(html);

        r1.setActionCommand("man");
        r2.setActionCommand("man1");
        r3.setActionCommand("man2");
        r4.setActionCommand("man3");


        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(r1);
        buttonGroup.add(r2);
        buttonGroup.add(r3);
        buttonGroup.add(r4);

        c.insets = new Insets(5,5,5,5);
        c.gridx = 0;
        c.gridy = 3;
        container.add(r1, c);

        c.gridx = 1;
        c.gridy = 3;
        container.add(r2, c);

        c.gridx = 0;
        c.gridy = 4;
        container.add(r3, c);

        c.gridx = 1;
        c.gridy = 4;
        container.add(r4, c);

        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;

        JButton bt = new JButton("Onayla");
        container.add(bt, c);
        bt.addActionListener((ActionEvent e) -> {


            if(userNameVal.getText().length()<3){
                userNameVal.setBorder(new MatteBorder(2,2,2,2, Color.RED));
                return;
            }


            if(buttonGroup.getSelection() == null) return;

            String icon = buttonGroup.getSelection().getActionCommand();
            user.setIcon(icon);
            logoLabel.setIcon(user.getUserIcon());

            user.setUserName(userNameVal.getText());
            context.setEnabled(true);
            nameValue.setText(user.getUserName());
            afterLogin.dispose();

            try {
                fms.WriteFileofUsers();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            context.validate();
            context.repaint();


        });
        c.gridwidth=1;




        afterLogin = new PopupScreen(this, container);
        container.setBackground(new Color(255, 255, 255, 200));
        container.setBorder(new MatteBorder(2, 2, 2, 2, Color.ORANGE));
        afterLogin.setVisible(true);
    }

    private void GUIInit() {
        ImageIcon image = new ImageIcon("src/images/bg.png");
        ImageIcon logo = new ImageIcon("src/images/man.png");
        ImageIcon levelBack = new ImageIcon("src/images/level.png");
        ImagePanel panel = new ImagePanel(image.getImage());
        Border border;
        Border margin;

        getContentPane().add(panel);
        panel.setLayout(new FlowLayout());

        /* LEFT VE RIGHT panellerini içerir. */
        JPanel container = new JPanel();
        container.setBackground(new Color(0, 0, 0, 0));

        JPanel right = new JPanel(new GridBagLayout());
        right.setPreferredSize(new Dimension(916, 600));
        right.setBackground(new Color(255, 255, 255, 80));

        levelButtons = new JLabel[maxLevel];

        GridBagConstraints gcc = new GridBagConstraints();
        gcc.fill = GridBagConstraints.HORIZONTAL;

        MouseListener listener = new MouseListener();

        //max levela göre düzeltilmeli giridx gridy
        for (int i = 0; i < levelButtons.length; ++i) {
            gcc.gridy = i / 5;
            gcc.gridx = i % 5;
            gcc.ipadx = 10;
            gcc.ipady = 10;
            gcc.insets = new Insets(10, 15, 0, 0);
            levelButtons[i] = new JLabel(String.valueOf(i + 1));
            levelButtons[i].setPreferredSize(new Dimension(115, 115));
            levelButtons[i].setIcon(levelBack);
            levelButtons[i].addMouseListener(listener);
            levelButtons[i].setHorizontalTextPosition(JLabel.CENTER);
            levelButtons[i].setVerticalTextPosition(JLabel.CENTER);
            levelButtons[i].setFont(levelFont);
            levelButtons[i].setFont(levelButtons[i].getFont().deriveFont(50.0f));
            levelButtons[i].setForeground(Color.decode("#FFD24D"));

            //levelButtons[i].setFont(new Font("TimesRoman",Font.PLAIN,48));
            right.add(levelButtons[i], gcc);
        }


      /*  for (int i = user.getLevel()+1; i < maxLevel+1; ++i) {

                levelButtons[i-1].setEnabled(false);
        }*/

        for (int i = user.getLevel(); i < maxLevel; ++i) {

            levelButtons[i].setEnabled(false);
        }

        JPanel left = new JPanel(new GridBagLayout());
        left.setPreferredSize(new Dimension(400, 738));
        left.setBackground(new Color(255, 255, 255, 0));


        container.add(left);
        container.add(right);
        panel.add(container);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;


        JPanel info = new JPanel(new GridBagLayout());
        info.setBackground(new Color(255, 255, 255, 150));

        GridBagConstraints c = new GridBagConstraints();

        logoLabel = new JLabel(user.getUserIcon());
        logoLabel.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        border = BorderFactory.createLoweredBevelBorder();
        margin = new EmptyBorder(10, 10, 10, 10);
        logoLabel.setBorder(new CompoundBorder(border, margin));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        info.add(logoLabel, c);

        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 0, 0);

        JLabel mail = new JLabel("Mail Adresi ");
        mail.setBorder(new MatteBorder(0, 0, 1, 0, Color.WHITE));
        border = mail.getBorder();
        margin = new EmptyBorder(0, 0, 2, 0);
        mail.setBorder(new CompoundBorder(border, margin));
        mail.setHorizontalAlignment(SwingConstants.LEFT);
        c.gridx = 0;
        c.gridy = 1;
        info.add(mail, c);

        JLabel userName = new JLabel("Kullanıcı Adı ");
        c.gridx = 0;
        c.gridy = 2;
        userName.setBorder(new MatteBorder(0, 0, 1, 0, Color.WHITE));
        border = userName.getBorder();
        margin = new EmptyBorder(0, 0, 2, 0);
        userName.setBorder(new CompoundBorder(border, margin));
        userName.setHorizontalAlignment(SwingConstants.LEFT);
        info.add(userName, c);

        JLabel level = new JLabel("Seviye Bilgisi");
        level.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        border = level.getBorder();
        margin = new EmptyBorder(10, 10, 10, 10);
        level.setBorder(new CompoundBorder(border, margin));
        level.setHorizontalAlignment(SwingConstants.CENTER);
        c.ipadx = 100;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        info.add(level, c);


        c.gridwidth = 0;
        c.insets = new Insets(10, 15, 0, 0);
        JLabel mailValue = new JLabel(user.getEmail());
        c.gridx = 1;
        c.gridy = 1;
        mailValue.setBorder(new MatteBorder(0, 0, 1, 0, Color.WHITE));
        border = mailValue.getBorder();
        margin = new EmptyBorder(0, 0, 2, 0);
        mailValue.setBorder(new CompoundBorder(border, margin));
        mailValue.setHorizontalAlignment(SwingConstants.CENTER);
        info.add(mailValue, c);

        nameValue = new JLabel(user.getUserName());
        c.gridx = 1;
        c.gridy = 2;
        nameValue.setBorder(new MatteBorder(0, 0, 1, 0, Color.WHITE));
        border = nameValue.getBorder();
        margin = new EmptyBorder(0, 0, 2, 0);
        nameValue.setBorder(new CompoundBorder(border, margin));
        nameValue.setHorizontalAlignment(SwingConstants.CENTER);
        info.add(nameValue, c);


        JLabel[] levelInfo;
        JLabel[] levelPoints;
        levelInfo = new JLabel[user.getLevel()];
        levelPoints = new JLabel[user.getLevel()];
        int k = 1;
        Map<Integer, Integer> levelMap = user.getLevelInfo();

        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 0, 0);
        for (int i = 0; i < user.getLevel(); ++i) {
            levelInfo[i] = new JLabel("Seviye " + String.valueOf(i + 1));
            levelInfo[i].setBorder(new MatteBorder(0, 3, 1, 0, Color.decode("#404244")));
            border = levelInfo[i].getBorder();
            levelInfo[i].setBorder(new CompoundBorder(border, new EmptyBorder(2, 2, 5, 2)));
            levelInfo[i].setHorizontalAlignment(SwingConstants.CENTER);
            String lvlP = String.valueOf(levelMap.get(i));
            if (lvlP.equals("null"))
                lvlP = "Oynanmamış";
            else lvlP += " P";


            levelPoints[i] = new JLabel(lvlP);
            levelPoints[i].setHorizontalAlignment(SwingConstants.CENTER);
            c.gridy = i + 4;
            c.gridx = 0;
            info.add(levelInfo[i], c);
            c.gridx = 1;
            info.add(levelPoints[i], c);
        }


        gc.gridx = 0;
        gc.gridy = 0;
        gc.ipady = 40;
        gc.ipadx = 40;
        left.add(info, gc);


        pack();
        setTitle("Minnoş");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1366, 790));
        setResizable(false);
        pack();
        setVisible(true);

    }

    private static class ImagePanel extends JPanel {

        private Image img;


        public ImagePanel(Image img) {
            this.img = img;
            Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
            setLayout(null);
        }

        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }

    }

    private class MouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int i = 1;
            for (JLabel button : levelButtons) {

                if (e.getSource().equals(button) && button.isEnabled()) {
                    user.setCurLevel(i);
                    loadLevel(getLevelName(i));
                    GameFrame gf = new GameFrame(user, grid, fms);
                    context.setVisible(false);
                    gf.setVisible(true);
                }
                i++;
            }
        }
    }
    private String getLevelName(int levelNo) {
        StringBuilder file = new StringBuilder();
        file.append("src/levels/level");
        file.append(levelNo + ".csv");
        return file.toString();
    }

    //dosya isimleri level1, level2 seklinde olacak. parametre olarak gelecek olan level ile dosya ismi ayni olacak
    private void loadLevel(String level) {
        String fileName = level;
        BufferedReader reader;
        String line;
        int rowNo = 0;
        try {
            File file = new File(fileName);
            reader = new BufferedReader(new FileReader(file));
            reader.readLine();
//            buffer = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String oneRow[] = line.split(",");
                fillTheGridRow(oneRow, rowNo);
                rowNo++;
            }
        } catch (IOException e) {
            System.out.println("Wrong file name");
        }

    }
    private void fillTheGridRow(String[] oneRow, int rowNo) {
        for (int i = 0; i < oneRow.length; ++i) {
            Cell theCell = createCell(oneRow[i], rowNo, i);
            theCell.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
            grid.get(rowNo).add(theCell);
        }
    }
    private Cell createCell(String theStr, int x, int y) {
        if (theStr.equals("TREE"))
            return new Cell(x, y, CellType.TREE);
        if (theStr.equals("CHARACTER"))
            return new Cell(x, y, CellType.CHARACTER);
        if (theStr.equals("FINISH"))
            return new Cell(x, y, CellType.FINISH);
        if (theStr.equals("PATH") || theStr.equals("NONE")) //CellType'da var ama henuz icerikleri olusturulmamis
            return new Cell(x, y, CellType.PATH);
        if (theStr.equals("BLOCK") || theStr.equals("FLOWER")) //CellType'da var ama henuz icerikleri olusturulmamis
            return new Cell(x, y, CellType.FLOWER);

        return null;
    }
}


