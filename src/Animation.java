import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

enum animType {
    Up, Down, Left, Right, Obstacle, RObstacle1
}

public class Animation {
    private JPanel cellPanel;
    private Cell character;
    private Cell characterBackUp;
    private ArrayList<ArrayList<Cell>> cellArray;
    private ArrayList<ArrayList<Cell>> cellBackUp;
    private int movePixel;
    private Timer timer;
    private Queue<animType> animQueue;
    private ArrayList<Cell> shortestPath;
    private int moveCount;
    private User user;
    private FileManagementSystem fms;
    private JFrame context;
    private boolean lastAnimBlocked;


    Animation(JPanel cellPanel, Cell character, ArrayList<ArrayList<Cell>> cellArray, FileManagementSystem fms, User user, JFrame context) {
        this.context = context;
        this.user = user;
        this.fms = fms;
        movePixel = 0;
        this.cellPanel = cellPanel;
        this.character = character;
        this.characterBackUp = new Cell(character.getCx(), character.getCy(), CellType.CHARACTER);
//        System.out.println(character.getCx() + " , " + character.getCy());
        this.cellArray = cellArray;
        cellBackUp = new ArrayList<>();
        for (int i = 0; i < cellArray.size(); ++i)
            cellBackUp.add(new ArrayList<>());
        for (int i = 0; i < cellArray.size(); ++i) {
            for (int j = 0; j < cellArray.get(0).size(); ++j) {
                Cell tmp = cellArray.get(i).get(j);
                cellBackUp.get(i).add(new Cell(tmp.getCx(), tmp.getCy(), tmp.getType()));
            }
        }

        GraphTools graphTools = new GraphTools();
        shortestPath = graphTools.calculateShortestPath(cellArray);
    }

    public void doAnimation(Queue<animType> animQueue) {
        if (animQueue == null || animQueue.size() == 0) return;
        this.animQueue = animQueue;
        moveCount = 0;

        animType type = animQueue.poll();

        if (type.equals(animType.Obstacle)) {
            if (lastAnimBlocked) {
                type = animQueue.poll();
                if (!isObstacle() && !animQueue.isEmpty())
                    type = animQueue.poll();
                lastAnimBlocked=false;
            } else {
                animQueue.poll();
                animQueue.poll();
                if (animQueue.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Minnoşu evine ulaştıramadın...Yardım butonuna tıklayarak yardım alabilirsin");
                    character.setIcon(user.getUserIcon());
                    GameFrame.stopSound();
                    return;
                }
            }
        }
        callAnims(type, 1);
        character.setIcon(new ImageIcon("src/images/" + user.getUserIconType() + ".gif"));
        GameFrame.playSound("walk2");
    }

    public void callAnims(animType type) {
        callAnims(type, 1);
    }

    public void callAnims(animType type, int length) {

        if (type.equals(animType.Obstacle)) {
            if (lastAnimBlocked) {
                if (!animQueue.isEmpty()) {
                    lastAnimBlocked = false;
                    callAnims(animQueue.poll());
                    return;

                }
            } else {
                animQueue.poll();
                if (animQueue.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Minnoşu evine ulaştıramadın...Yardım butonuna tıklayarak yardım alabilirsin");
                    character.setIcon(user.getUserIcon());
                    GameFrame.stopSound();
                    return;
                }else{
                    lastAnimBlocked=false;
                    callAnims(animQueue.poll());
                    return;
                }
            }
        }
        moveCount++;
        if (movePixel != 0) return;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                makeMoves(length, type);
            }
        }, 0, 10);
    }

    private void makeMoves(int length, animType direction) {
        movePixel++;
        int x = 0, y = 0, x2 = 0, y2 = 0;
        int cx = character.getCx();
        int cy = character.getCy();
        boolean illegalMove = false;
        switch (direction) {
            case Up:
                y = -1;
                x = 0;
                x2 = -1;
                y2 = 0;
                if (cx == 0) {
                    illegalMove = true;
                }
                break;
            case Down:
                y = +1;
                x = 0;
                x2 = +1;
                y2 = 0;
                if (cx >= cellArray.size() - 1) {
                    illegalMove = true;
                }
                break;
            case Right:
                y = 0;
                x = +1;
                x2 = 0;
                y2 = 1;
                if (cy >= cellArray.get(0).size() - 1) {
                    illegalMove = true;
                }
                break;
            case Left:
                y = 0;
                x = -1;
                x2 = 0;
                y2 = -1;
                if (cy <= 0) {
                    illegalMove = true;
                }
                break;
        }

        if (illegalMove) {
            JOptionPane.showMessageDialog(cellPanel, "Minnoş bu hamleyi beğenmedi...");
            animQueue.clear();
            stopAnimation();
            character.setIcon(user.getUserIcon());
            for (int i = 0; i < cellBackUp.size(); ++i) {
                for (int j = 0; j < cellBackUp.get(0).size(); ++j) {
                    if (cellBackUp.get(i).get(j).isCharacter()) {
                        cellArray.get(i).get(j).setType(CellType.CHARACTER);
                        character = cellArray.get(i).get(j);
                        character.setIcon(user.getUserIcon());
                    } else
                        cellArray.get(i).get(j).setType(cellBackUp.get(i).get(j).getType());
                }
            }
            return;
        }

        int width = character.getWidth();
        Cell forward = cellArray.get(character.getCx() + x2).get(character.getCy() + y2);
        if (!isLegal(forward)) {
            stopAnimation();
            lastAnimBlocked = true;
            if (!animQueue.isEmpty()) {
               // GameFrame.playSound("walk2");
                callAnims(animQueue.poll());
            }
            return;
        }

        character.setLocation(character.getLocation().x + x, character.getLocation().y + y);

        forward.setVisible(false);

        if (movePixel == width * length) {
            Object[] options = {"Tamam"};
            int ret;
            if (forward.getType() == CellType.FINISH) {
                stopAnimation();
                GameFrame.stopSound();
                character.setIcon(user.getUserIcon());
                forward.setVisible(true);
                for (Cell aShortestPath : shortestPath) {
                    int xx = aShortestPath.x;
                    int yy = aShortestPath.y;
                    if (cellArray.get(xx).get(yy).getType() != CellType.FINISH)
                        cellArray.get(xx).get(yy).setIcon(new ImageIcon("src/images/solvedPath.png"));
                }
                int point = 0;
                if (moveCount <= shortestPath.size()) {
                    GameFrame.playSound("goodmove");
                    ret = JOptionPane.showOptionDialog(cellPanel, "Tebrikler, bu bölümde en kısa yollardan birini kullanarak tam puan aldın!", "Hedefe Ulaştın", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    point = 10;
                } else {
                    GameFrame.playSound("badmove");
                    ret = JOptionPane.showOptionDialog(cellPanel, "Tebrikler sona geldin fakat minnoşu daha fazla yormadan evine götürebilirdin.\nSenin için bu yolu işaretledik mutlaka bir göz at.", "Hedefe Ulaştın", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    point = shortestPath.size() - moveCount;
                    if (point < 1) point = 1;
                }
                if (ret == 0) {
                    if (user.getCurLevel() == user.getLevel())
                        user.setLevel(user.getLevel() + 1);
                    user.setLevelInfo(point);
                    PrepareGame pre = new PrepareGame(user, fms);
                    pre.setVisible(true);
                    context.dispose();
                    try {
                        fms.WriteFileofUsers();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return;
            } else {
                forward.setVisible(true);
                forward.setBorder(null);
                character.setBorder(BorderFactory.createLineBorder(Color.WHITE));

                stopAnimation();
                Icon charIcon = character.getIcon();
                character.setType(CellType.PATH);
                forward.setType(CellType.CHARACTER);
                forward.setIcon(charIcon);
                character = forward;

                cellPanel.revalidate();
                cellPanel.repaint();
                if (!animQueue.isEmpty()) {
                    GameFrame.playSound("walk2");
                    callAnims(animQueue.poll());
                } else {
                    JOptionPane.showMessageDialog(null, "Minnoşu evine ulaştıramadın...Yardım butonuna tıklayarak yardım alabilirsin");
                    character.setIcon(user.getUserIcon());
                    GameFrame.stopSound();
                    for (int i = 0; i < cellBackUp.size(); ++i) {
                        for (int j = 0; j < cellBackUp.get(0).size(); ++j) {
                            if (cellBackUp.get(i).get(j).isCharacter()) {
                                cellArray.get(i).get(j).setType(CellType.CHARACTER);
                                character = cellArray.get(i).get(j);
                                character.setIcon(user.getUserIcon());
                            } else
                                cellArray.get(i).get(j).setType(cellBackUp.get(i).get(j).getType());
                        }
                    }
                }
            }
        }
    }

    private void stopAnimation() {
        movePixel = 0;
        timer.cancel();

    }

    private boolean isLegal(Cell cell) {
        return (cell.getType() == CellType.PATH || cell.getType() == CellType.FINISH);
    }

    private boolean isObstacle() {
        int x = character.getCx();
        int y = character.getCy();
        boolean ret = false;
        if (x - 1 >= 0 && (cellArray.get(x - 1).get(y).getType() == CellType.BLOCK || cellArray.get(x - 1).get(y).getType() == CellType.TREE)) {
            ret = true;
        }
        if (!ret) {
            if (x + 1 < cellArray.size() && (cellArray.get(x + 1).get(y).getType() == CellType.BLOCK || cellArray.get(x + 1).get(y).getType() == CellType.TREE))
                ret = true;
        }
        if (!ret) {
            if (y + 1 < cellArray.get(0).size() && (cellArray.get(x).get(y + 1).getType() == CellType.BLOCK || cellArray.get(x).get(y + 1).getType() == CellType.TREE))
                ret = true;
        }

        if (!ret) {
            if (y - 1 >= 0 && (cellArray.get(x).get(y - 1).getType() == CellType.BLOCK || cellArray.get(x).get(y - 1).getType() == CellType.TREE))
                ret = true;
        }
        return ret;
    }

}
