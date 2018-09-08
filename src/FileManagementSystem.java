import java.io.*;
import java.util.ArrayList;

public class FileManagementSystem {
    private ArrayList<User> users;

    public ArrayList<User> getUsers() {
        return users;
    }

    public void ReadFileofUsers(){
        String fileName = "src/Users.csv";
        String line = "";
        String csvSplitBy = ";";
        File usersFile = new File(fileName);

        users = new ArrayList<>();

        if(!usersFile.exists()){
            System.out.println("Unable to open file");
        }
        else{
            try{
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                ArrayList<Integer> points = null ;
                while((line = br.readLine()) != null){
                    String[] userInf = line.split(csvSplitBy);
                    int length = Integer.parseInt(userInf[4]);
                    points = new ArrayList<>();
                    for (int i = 5 ; i < userInf.length ; ++i)
                        points.add(Integer.parseInt(userInf[i]));
                    users.add(new User(userInf[0],userInf[1],userInf[2],userInf[3],points));
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *  dosya ya yazma tamam. csv dosyasını proje icinde database klasoru icine attım ordan cekiom istedigin kadar level gir sıkıntı yok.
     *  kacıncı levelde kaldıysan onu csv den deistir. kac puan oldugunu sayıyor tekrardan level sutununa yazıo test edebilirsiniz.
     *  Static yapma sebebim diger metodlardan ulasak objeyi olusturmadan
     *  users arraylistinide bu yuzden static yaptım.
     * @throws IOException
     */
    public void WriteFileofUsers() throws IOException{
        String fileName = "src/Users.csv";
        String camma = ";";
        File usersFile = new File(fileName);
        FileWriter roomsWriter = new FileWriter(usersFile,false);
        if(!usersFile.exists()){
            usersFile.createNewFile();
        }

        try (BufferedWriter usersInf = new BufferedWriter(roomsWriter)) {

            for(int i = 0; i < users.size(); i++){
                String puans = "";
                for(int j = 0; j < users.get(i).getLevelInfo().size(); j++) {
                    //System.out.println((users.get(i).levelInfo.get(j)));
                    puans += (users.get(i).getLevelInfo().get(j));
                    if(j != users.get(i).getLevelInfo().size())
                        puans += camma;

                }
                usersInf.write(users.get(i).getUserName()+camma+users.get(i).getEmail()+camma
                        +users.get(i).getPassword()+camma+users.get(i).getUserIconType()+camma+users.get(i).getLevel()+camma+puans+"\n");

            }
        }
    }

}
