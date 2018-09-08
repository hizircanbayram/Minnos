import javax.swing.*;
import java.util.LinkedList;
import java.util.Random;

enum CellType {WALL, TREE, PATH, CHARACTER, FINISH, BLOCK, FLOWER}

public class Cell extends JLabel {
    protected int x;
    protected int y;
    private CellType type;
    private Icon tree = new javax.swing.ImageIcon(getClass().getResource("images/tree.png"));
    private Icon character = new javax.swing.ImageIcon(getClass().getResource("images/man.png"));
    private Icon path = new javax.swing.ImageIcon(getClass().getResource("images/path.png"));
    private Icon finish = new javax.swing.ImageIcon(getClass().getResource("images/finish.png"));
    private Icon block = new javax.swing.ImageIcon(getClass().getResource("images/block.jpg"));
    private Object obj;

    public int getCx() {
        return x;
    }

    public int getCy() {
        return y;
    }

    Cell(int x, int y, CellType type) {
        super();
        this.x = x;
        this.y = y;
        this.type = type;
        this.setImage();
    }

    private void setImage() {
        switch (type) {
            case TREE:
                int t = new Random().nextInt(5);
                if (t == 0)
                    this.setIcon(tree);
                else this.setIcon(new ImageIcon("src/images/plant" + t + ".png"));
                break;
            case CHARACTER:
                this.setIcon(character);
                break;
            case PATH:
                this.setIcon(path);
                break;
            case FINISH:
                this.setIcon(finish);
                break;
            case BLOCK:
                this.setIcon(block);
                break;

        }
    }

    public CellType getType() {
        return type;
    }

    public boolean isCharacter() {
        return (type == CellType.CHARACTER);
    }

    public boolean isTree() {
        return (type == CellType.TREE);
    }

    public boolean isFinish() {
        return (type == CellType.FINISH);
    }

    public boolean isBLock() {
        return (type == CellType.BLOCK);
    }

    public boolean isPath() {
        return (type == CellType.PATH);
    }

    public void setType(CellType type) {
        this.type = type;
        setImage();
    }

    public void setCx(int x) {
        this.x = x;
    }

    public void setCy(int y) {

        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Cell))
            return false;
        this.obj = obj;
        Cell other = (Cell) obj;
        return x == other.x && y == other.y;
    }
}