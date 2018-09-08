
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class GameFrame extends JFrame {
    private JFrame context = this;

    private Compiler compiler;
    private Queue<animType> statementQueue;
    private ArrayList<ArrayList<Cell>> cell;
    private Cell character;
    private User user;
    private int level;
    ImageIcon logo;
    private ArrayList<String> messages;
    private FileManagementSystem fms;

    private JButton click;
    private JButton home;
    private JButton preparegame;
    private Animation anim;
    private String headerText;

    private JTextArea codeText;

    GameFrame(User user, ArrayList<ArrayList<Cell>> cell, FileManagementSystem fms) {


        Font userFont=null;
        Font headerFont=null;
        try {
            userFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Meatloaf5.ttf"));
            headerFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Meatloaf.ttf"));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        this.user = user;
        this.level = user.getLevel();
        this.cell = cell;
        this.fms = fms;
        readStrings();

        logo = new ImageIcon("src/images/man.png");


        context.setSize(1366, 768);
        context.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ImageIcon image = new ImageIcon("src/images/bg.png");
        ImagePanel container = new ImagePanel(image.getImage());
        container.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel topSol = new JPanel();
        topSol.setBackground(new Color(0,0,0,0));
        topSol.setLayout(new BoxLayout(topSol,BoxLayout.Y_AXIS));
        topSol.setBorder(new EmptyBorder(1,20,0,0));


        JLabel username = new JLabel(user.getUserName());
        username.setIcon(new ImageIcon("src/images/user.png"));
        JLabel level = new JLabel(String.valueOf(user.getLevel()));
        level.setIcon(new ImageIcon("src/images/levelicon.png"));

        username.setForeground(Color.WHITE);
        username.setFont(userFont.deriveFont(45f));

        level.setForeground(Color.WHITE);
        level.setFont(userFont.deriveFont(45f));

        topSol.add(username);
        topSol.add(level);



        topPanel.add(topSol,BorderLayout.WEST);

        JLabel header = new JLabel(headerText);
        header.setFont(headerFont.deriveFont(88f));
        header.setForeground(Color.decode("#116939"));
        topPanel.add(header,BorderLayout.CENTER);
        header.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel help = new JLabel(new ImageIcon("src/images/help.png"));

        topPanel.add(help,BorderLayout.EAST);


        //label.setText(user.toString());
        //label.setFont(new Font("Courier New", 0, 24));
        //topPanel.add(label);
        topPanel.setPreferredSize(new Dimension(1366, 110));
        topPanel.setBackground(new Color(255,255,255,120));
        topPanel.setBorder(new MatteBorder(0,0,2,0,Color.WHITE));
        container.add("North", topPanel);


        //Main Panel - Kod ve Oyun panellerini içerir.
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(0,0,0,0));
        mainPanel.setBorder(new EmptyBorder(15,10,10,10));

        JPanel codePanel = new JPanel();
        codePanel.setLayout(new FlowLayout());
        codePanel.setBackground(new Color(0,0,0,0));
        codePanel.setPreferredSize(new Dimension(400, 610));
        mainPanel.add("West", codePanel);

        compiler = new Compiler(mainPanel);

        codeText = new JTextArea();
        codeText.setBackground(Color.decode("#F6F6F6"));
        codeText.setWrapStyleWord(true);
        codeText.setLineWrap(true);
        codeText.setColumns(25);
        codeText.setRows(19);
        codeText.setFont(new Font("Courier New", 0, 24));
        codeText.setBorder(new LineBorder(Color.WHITE, 5));
        JScrollPane taScroll = new JScrollPane(codeText);
        taScroll.setBorder(new LineBorder(Color.WHITE,5));
        codePanel.add(taScroll);
        buttonListener buttonListener = new buttonListener();
        /**
         * Buralar düzenlenecek dikkat et!
         */
        home = new JButton("Ana Sayfaya Dön");
        preparegame = new JButton("Seviye Seç");
        click = new JButton("Derle ve Çalıştır");

        codePanel.add(home);
        codePanel.add(preparegame);
        codePanel.add(click);


        preparegame.addActionListener(buttonListener);
        home.addActionListener(buttonListener);
        click.addActionListener(buttonListener);
        help.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                MakeDialog frame = new MakeDialog(logo, messages);
                frame.setVisible(true);
                playSound("button");
            }
        });

        JPanel animationPanel = new JPanel();
        animationPanel.setBackground(new Color(255,255,255,0));
        animationPanel.setLayout(new FlowLayout());
        animationPanel.setPreferredSize(new Dimension(940, 610));
        animationPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        mainPanel.add("East", animationPanel);

        JPanel cellPanel = new JPanel();
        cellPanel.setLayout(new GridLayout(7, 10));
        cellPanel.setBackground(Color.decode("#F6F6F6"));
        cellPanel.setBorder(new LineBorder(Color.WHITE, 4));
        animationPanel.add(cellPanel);



        //Board oluşturma ve ekranda gösterme

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 10; j++) {
                Cell ch = cell.get(i).get(j);
                if (ch.isCharacter()) {
                    ch.setIcon(user.getUserIcon());
                    character = ch;
                }
                cellPanel.add(cell.get(i).get(j));
            }
        }


        anim = new Animation(cellPanel, character, cell,fms,user,context);


        container.add("Center", mainPanel);

        context.add(container);
        context.setLocationRelativeTo(null);
        context.setResizable(false);
        context.setVisible(true);
    }

    private class buttonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == home) {
                Home home = new Home();
                home.setVisible(true);
                context.dispose();
                playSound("back");
            }
            if (e.getSource() == preparegame) {
                PrepareGame pre = new PrepareGame(user, fms);
                pre.setVisible(true);
                context.dispose();
                playSound("back");

            }
            if (e.getSource() == click) {
                if (character == null) {
                    JOptionPane.showMessageDialog(context, "Karakter boş");
                }


                String text = codeText.getText();
                if(!text.equals(""))
                    statementQueue = compiler.process(text);
                if (statementQueue == null)
                    codeText.setText("");
                else
                    anim.doAnimation(statementQueue);
            }

        }

    }

    private void readStrings() {
        String fileName = "src/levelTexts/level" + user.getCurLevel() + ".txt";
        String line = "";
        File usersFile = new File(fileName);
        if (!usersFile.exists()) {
            System.out.println("Unable to open file");
        } else {
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                headerText =  br.readLine();
                messages = new ArrayList<>();
                int i = 0;
                while ((line = br.readLine()) != null) {
                    messages.add(line);
                    i++;
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class MakeDialog extends JPanel {
        private JLabel icon;
        private JTextPane message;
        private JButton button;
        private ArrayList<String> userInfoMessages;
        int msgCounter;
        private PopupScreen ps;

        private MakeDialog(ImageIcon icon, ArrayList<String> buttonMsg) {
            msgCounter = 0;
            userInfoMessages = buttonMsg;

            this.icon = new JLabel(icon);

            button = new JButton(userInfoMessages.size() == 1 ? "Anladım" : "İleri");

            message = new JTextPane();
            message.setText(buttonMsg.get(msgCounter));

            ps = new PopupScreen(context, this, 500, 360);
            setBackground(new Color(255, 255, 255, 0));
            setBorder(new MatteBorder(10, 10, 10, 10, Color.WHITE));
            ps.setVisible(true);

            setTitle("Minnoş");


            initGui();
        }

        private void initGui() {
            setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;

            //picture
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 0;
            c.weightx = 1;
            panel.add(icon, c);

            //text
            c.gridx = 0;
            c.gridy = 1;
            c.ipadx = 20;
            c.insets = new Insets(10, 10, 10, 10);


            message.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            message.setOpaque(false);
            message.setEditable(false);
            message.setFont(new Font("Corbel", Font.BOLD, 18));

            panel.add(message, c);

            //button
            c.ipadx = 5;
            c.ipady = 5;
            c.gridx = 0;
            c.gridy = 2;
            c.insets = new Insets(20, 0, 0, 0);
            add(panel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BorderLayout());
            JLabel next = new JLabel();
            next.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    msgCounter++;
                    if (msgCounter == userInfoMessages.size()) {
                        context.setEnabled(true);
                        ps.dispose();
                    } else {
                        playSound("button");
                        message.setText(userInfoMessages.get(msgCounter));
                    }
                }
            });

            JLabel close = new JLabel(new ImageIcon("src/images/close.png"));
            close.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    context.setEnabled(true);
                    ps.dispose();
                    playSound("back");

                }
            });

            next.setIcon(new ImageIcon("src/images/next.png"));
            buttonPanel.add(close,BorderLayout.WEST);
            buttonPanel.add(next,BorderLayout.EAST);
            buttonPanel.setBorder(new EmptyBorder(0,10,10,10));

            add(buttonPanel, BorderLayout.SOUTH);
            System.out.println();
        }


    }
    private static Clip clip;
    public static synchronized void playSound(String name) {
        new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
            public void run() {
                try {
                    clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("src/sounds/" + name +".wav"));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
    public static synchronized void stopSound() {
        new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
            public void run() {
                try {
                   clip.stop();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }





}
