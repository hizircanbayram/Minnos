import java.util.ArrayList;

public class TheObject {
    private String name;
    private String dataString;
    private Integer data;

    public TheObject(String name, String dataString) {
        this.name = name;
        this.dataString = dataString;
    }

    public boolean isNameEmpty() {
        int i;
        for (i = 0; i < name.length(); ++i) {
            if (!Character.isSpaceChar(name.charAt(i)))
                break;
        }
        return i == name.length();
    }

    public String getName() {
        return name;
    }

    public String getDataString() {
        return dataString;
    }

    public Integer getData() {
        return data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public void incrementData() {
        ++data;
    }

    public void decrementData() {
        --data;
    }
    /**
     * Nesnenin isminin uygun olup olmadigi kontrol ediliyor. Nesne ismi sayi ile baslayamaz, sayi ve harf disinda bir sey iceremez
     * @param theName
     * @return
     */
    public boolean nameAnalyser(String theName) {
        if (!Character.isAlphabetic(theName.charAt(0))) {
            //System.out.println("Identifier sayi ile baslayamaz!!");
            return false;
        }
        for (int i = 1; i < theName.length(); ++i) {
            if (!(Character.isDigit(theName.charAt(i)) || Character.isAlphabetic(theName.charAt(i)))) {
                //System.out.println("Identifier'da sayi ve harf disinda karakter olamaz!!");
                return false;
            }
        }
        return true;
    }

    /**
     * TheObject turden nesnenin name ve dataString field'larinin icindeki bosluklari siler. Boylece nesne kendi ismini almis olur
     * dataString ise sayi mi degil mi gibi kontrollere acik hale getirilir.
     */
    public void deleteSpacesInObject() {
        StringBuilder nonEmptyName = new StringBuilder();
        String[] ar = this.name.split(" ");
        //System.out.println("sa");
        if (ar.length > 1) {
            //int a b = 5 gibi durumlar icin
            //System.out.println("Tanimlanamayan degisken ismi!!");
            return;
        }
        for (int i = 0; i < this.name.length(); ++i) {
            if (!Character.isSpaceChar(this.name.charAt(i)))
                nonEmptyName.append(this.name.charAt(i));
        }
        this.name = nonEmptyName.toString();
        StringBuilder nonEmptyDataString = new StringBuilder();
        /*ilklendirilmemis nesnenin data'si olmayacagi icin dataAnalyser'a sokamayiz. Ancak ismi olmak zorunda oldugundan
        * ilklendirilmemis degiskenlerin data'sinin atamasini burada yapiyoruz. Eger buraya gelen nesnenin dataString'i yok ise,
        * data'sia otomatik olarak 0 ataniyor.*/
        if (this.dataString == null)
            this.data = 0;
        else {
            //int a = c 5 gibi durumlar icin
            if (this.dataString.split(" ").length > 1) {
                //System.out.println("Tanimlanamayan rvalue!!");
                return;
            }
            for (int i = 0; i < this.dataString.length(); ++i) {
                if (!Character.isSpaceChar(this.dataString.charAt(i)))
                    nonEmptyDataString.append(this.dataString.charAt(i));
            }
            this.dataString = nonEmptyDataString.toString();
        }
    }

    /**
     * Eger dataString int turden ise atama yapilir ve return dondurulur. int turden degil ise exception handle edilir.
     * Baska bir degiskenin ismi rvalue olmus olabilir bu durum kontrol edilir. Eger bu sekildeyse o nesnenin data'si atanir
     * Eger bu da degilse tanimlanmamis bir deger atanmaya calisiliyordur ve 'Undefined rvalue!!' yazdirilir.
     * @param obj
     * @return
     */
    public boolean dataAnalyser(ArrayList<TheObject> obj) {
        try {
            setData(Integer.parseInt(dataString));
            return true;
        } catch (Exception io) {
            //ikinci kosul, kendinden once tanimlanan degiskenlerin data'sinin atanabilmesini sagliyor. sinirlama getiriyor
            //pek bir boka da yaramamis olabilir, buna bir daha bakmak lazim
            for (int i = 0; i < obj.size() && !this.name.equals(obj.get(i).getName()); ++i) {
                if (obj.get(i).getName().equals(this.dataString)) {
                    this.data = obj.get(i).data;
                    return true;
                }
            }
            //System.out.println("Undefined rvalue!!");
            return false;
        }
    }
}
