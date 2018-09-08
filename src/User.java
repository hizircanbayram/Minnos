import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class User {
    private String userName;
    private String email;
    private String password;
    private int level;
    private int curLevel;
    private Map<Integer, Integer> levelInfo;
    private ImageIcon userIcon;
    private String userIconType;

    public User(String userName, String email, String password,String iconType,ArrayList<Integer> points) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        levelInfo = new TreeMap<>();
        for(int i = 0 ; i < points.size() ; ++i)
            levelInfo.put(i,points.get(i));
        level = points.size()+1;
        if(points.size()==0){
            level=1;
        }

        curLevel = -1;
        setIcon(iconType);
    }

    public void setIcon(String iconType){
        userIconType = iconType;
        String iconPath = "src/images/" + iconType + ".png";
        userIcon = new ImageIcon(iconPath);
    }

    public Map<Integer,Integer> getLevelInfo(){
        return levelInfo;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
    @Override
    public String toString(){
        return "Kullanıcı : " + userName + " ,Level : " + level + " mail : " + email + "sifre : " + password + "\n";
    }

    public ImageIcon getUserIcon() {
        return userIcon;
    }

    public void setCurLevel(int curLevel) {
        this.curLevel = curLevel;
    }

    public int getCurLevel() {
        return curLevel;
    }

    public String getUserIconType() {
        return userIconType;
    }

    public void setUserIconType(String userIconType) {
        this.userIconType = userIconType;
    }

    public int totalPoint(){
        int total=0;
        int level=this.getLevel();

        for(int i=0;i<level-1;++i){
            total+=this.getLevelInfo().get(i);
        }
        return total;
    }
    public void setLevelInfo(int point){
        levelInfo.put((curLevel-1),point);
    }
}
