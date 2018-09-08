import javax.swing.*;
import java.awt.*;

public class PopupScreen extends JFrame {
    private Component fromScreen;

    PopupScreen(Component source, Component container) {
        this(source, container, 480, 360);
    }

    PopupScreen(Component source, Component container, int x, int y) {
        fromScreen = source;
        setTitle("Test");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(x, y);

        JPanel borderLayoutPanel = new JPanel(new BorderLayout());
        add(borderLayoutPanel);

        fromScreen.setEnabled(false);

        borderLayoutPanel.setBackground(new Color(255, 255, 255, 200));
        setUndecorated(true);
        setLocationRelativeTo(null);
        setOpacity(0.90f);
        toFront();
        setAlwaysOnTop(true);
        setResizable(false);
        borderLayoutPanel.add(container);



    }

}
