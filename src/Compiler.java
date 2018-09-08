import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

enum errorType {
    UnexpectedIdentifier, IdentifierAfterOperator, WrongUsageOfOperator
}
//DOSYADA HICBIR SEY YOKKEN DE SENTAKS HATASI VERIYOR OLABILIR
public class Compiler {

    private Queue<String> statementQueue;
    private Queue<SyntaxError> allErrors = new LinkedList<>();
    private final String[] allMethodsIdentifiers = new String[]{"asagiAdimAt", "yukariAdimAt", "sagaAdimAt", "solaAdimAt", "engelVar"};
    private JPanel context;
    private ArrayList<TheObject> objects;
    private ArrayList<String> errors = new ArrayList<>();
    private int loopNumber = 0;
    private Integer loopDifference = 0;
    Queue<animType> statementAnim = new LinkedList<animType>();

    enum errorTypes {declaration, operation, function};

    public Compiler(JPanel panel) {
        context = panel;
        objects = new ArrayList<>();
    }

    /**
     * Gelen kodlari expression'lara boler, her expression'in bir if kosulu mu, for dongusu mu, fonksiyon cagrisi mi, deklarasyon
     * mu, yoksa operatorsel islemler mi olduguna karar verir, bunlara uygun islemleri yaptirir, sonucunda hangi fonksiyondan
     * kac tane uretilmisse queue'ya yazar ve bunu GameFrame sinifina gondeririz.
     * @param userInput
     */
    public Queue<animType> process(String userInput) {
        userInput = userInput.replace("\n","");
        userInput = userInput.replace("\t","");
        errorTypes theError;
        String[] expression = getLoopedVersion(userInput);
        StringBuilder errorMessages = new StringBuilder();
        boolean givenError = false;
        for (int i = 0; i < expression.length; ++i) {
            expression[i] = deleteSpacesForParsing(expression[i]);
            if (isDeclaration(expression[i]) && !isUnproperCharacterInExpression(expression[i])) {
                theError = errorTypes.declaration;
                manageDeclarationProcess(expression[i]);
            }
            else if (isIfExpression(expression[i], i)) {

            }
            else if (isWhileExpression(expression, expression[i], i)) {
                ArrayList<String> cnt = new ArrayList<>();
                for (int k = i + 1; k < expression.length && expression[k].charAt(0) == '*'; ++k)
                    cnt.add(expression[k]);
                for (int m = 0; m < loopNumber; ++m) {
                    for (int n = 0; n < cnt.size(); ++n) {
                        if (isFunction(cnt.get(n).substring(1), i) || isIfExpression(cnt.get(n).substring(1), i))
                            addStatementAnim(cnt.get(n).substring(1));
                        if (isOperatorExpression(cnt.get(n).substring(1)))
                            makeOperator(cnt.get(n).substring(1));
                    }

                }
            }
            else if (expression[i].charAt(0) == '*') {
                continue;
            }
            else if (isOperatorExpression(expression[i]) && makeOperator(expression[i])) {
                //bos olacak
            }
            else if (isFunction(expression[i], i)) {
                addStatementAnim(expression[i]);
            }
            else {
                errorMessages.append(i + 1 + ". ifadede hata var!\n");
                givenError = true;
            }
        }
        if(statementAnim.isEmpty() && !givenError)
            showErrorMessage("Minnoş bunu anlamadı.");
        else if(givenError) {
            showErrorMessage(errorMessages.toString());
            return null;
        }
        return statementAnim;
    }

    private String[] getLoopedVersion(String input) {
        if (input == null || input.equals(""))
            return null;

        int startIndex = input.indexOf("{");
        int endIndex = input.indexOf("}");
        if (startIndex == -1 || endIndex == -1) {
            return input.split(";");
        }

        String expression = input.substring(0, startIndex);
        String loop = input.substring(startIndex + 1, endIndex);

        String[] ret = getLoopedVersion(input.substring(endIndex + 1));

        String[] nonLoop = expression.split(";");
        String[] inLoop = loop.split(";");

        String[] res = new String[nonLoop.length + inLoop.length + (ret == null ? 0 : ret.length)];
        int k = 0;
        for (int i = 0; i < nonLoop.length; ++i) {
            nonLoop[i] = deleteSpacesForParsing(nonLoop[i]);
            res[k++] = nonLoop[i];
        }
        for (int i = 0; i < inLoop.length; ++i) {
            inLoop[i] = deleteSpacesForParsing(inLoop[i]);
            res[k++] = "*" + inLoop[i];
        }
        if (ret != null) {
            if (ret.length == 1) {
                ret = ret[0].split(";");
            }
            for (String aRet : ret) {
                res[k++] = aRet;
            }
        }
        return res;
    }

    private boolean isWhileExpression(String[] exp, String expression, int i) {
        if (expression.length() < 8)
            return false;
        if (!expression.substring(0, 5).equals("while")) //while
            return false;
        int begin = findFirstCharacter(expression, 5, ' ');
        if (expression.charAt(begin) != '(')
            return false;
        int end = findFirstChar(expression, begin + 1, ')');
        String relation = expression.substring(begin + 1, end);
        relation = deleteSpacesForParsing(relation);
        try {
            loopNumber = Integer.parseInt(relation);
            return true;
        } catch (Exception io) {
            if (isVariableWhile(relation))
                return true;
            int indice = -1;
            if ((indice = higher(relation)) != -1) { //kasten atama yapildi. herhangi yanlislik yok
                //"   i  >   2 "
                String firstOperand = relation.substring(0, indice);
                firstOperand = leaveSpacesInCommand(firstOperand);
                String secondOperand = relation.substring(indice + 1, relation.length());
                secondOperand = leaveSpacesInCommand(secondOperand);
                try {
                    Integer firstValue = 0;
                    int m;
                    for (m = 0; m < objects.size(); ++m) {
                        if (objects.get(m).getName().equals(firstOperand)) {
                            firstValue = objects.get(m).getData();
                            break;
                        }
                    }
                    if (m == objects.size())
                        return false;
                    Integer secondValue = Integer.parseInt(secondOperand);
                    loopNumber = firstValue - secondValue;
                    ArrayList<String> str = new ArrayList<>();
                    for (int k = i + 1; k < exp.length && exp[k].charAt(0) == '*'; ++k) {
                        str.add(exp[k]);
                        String theExp = leaveSpacesInCommand(exp[k]);
                        if (isOperatorExpression(theExp.substring(1)) && theExp.substring(1, 3).equals("--") && theExp.substring(3).equals(firstOperand))
                            return true;
                    }
                    return false;
                } catch (Exception oi) {
                    return false;
                }
            }
            else if ((indice = lower(relation)) != -1) {
                String firstOperand = relation.substring(0, indice);
                firstOperand = leaveSpacesInCommand(firstOperand);
                String secondOperand = relation.substring(indice + 1, relation.length());
                secondOperand = leaveSpacesInCommand(secondOperand);
                try {
                    Integer firstValue = 0;
                    int m;
                    for (m = 0; m < objects.size(); ++m) {
                        if (objects.get(m).getName().equals(firstOperand)) {
                            firstValue = objects.get(m).getData();
                            break;
                        }
                    }
                    if (m == objects.size())
                        return false;
                    Integer secondValue = Integer.parseInt(secondOperand);
                    loopNumber = secondValue - firstValue;
                    ArrayList<String> str = new ArrayList<>();
                    for (int k = i + 1; exp[k].charAt(0) == '*'; ++k) {
                        str.add(exp[k]);
                        String theExp = leaveSpacesInCommand(exp[k]);
                        if (isOperatorExpression(theExp) && theExp.substring(1, 3).equals("++") && theExp.substring(3).equals(firstOperand))
                            return true;
                    }
                    return false;
                } catch (Exception oi) {
                    return false;
                }
            }
            else {
                return false;
            }
        }
    }

    private int lower(String relation) {
        for (int i = 0; i < relation.length(); ++i) {
            if (relation.charAt(i) == '<')
                return i;
        }
        return -1;
    }

    private int higher(String relation) {
        for (int i = 0; i < relation.length(); ++i) {
            if (relation.charAt(i) == '>')
                return i;
        }
        return -1;
    }

    private boolean isVariableWhile(String relation) {
        for (int k = 0; k < objects.size(); ++k) {
            if (objects.get(k).getName().equals(relation)) {
                loopNumber = objects.get(k).getData();
                return true;
            }
        }
        return false;
    }

    private void addStatementAnim(String expression) {
        if (expression.equals("asagiAdimAt()"))
            statementAnim.add(animType.Down);
        else if (expression.equals("sagaAdimAt()"))
            statementAnim.add(animType.Right);
        else if (expression.equals("solaAdimAt()"))
            statementAnim.add(animType.Left);
        else if (expression.equals("yukariAdimAt()"))
            statementAnim.add(animType.Up);
        else if (expression.equals("engelVar()"))
            statementAnim.add(animType.Obstacle);

    }

    private String leaveSpacesInCommand(String temp) {
        int last_indice, first_indice = 0;
        if (temp.charAt(0) == ' ')
            first_indice = findFirstIndice(temp);
        for (last_indice = first_indice; last_indice < temp.length(); ++last_indice) {
            if (temp.charAt(last_indice) == ' ')
                break;
        }
        //System.out.println(first_indice + " " + last_indice);
        return temp.substring(first_indice, last_indice);
    }

    private int findFirstIndice(String temp) {
        int indice;
        for (indice = 0; (temp.charAt(indice) == ' ') || (temp.charAt(indice) == '\t'); ++indice)
            ;
        return indice;
    }

    private void parse(String userInput) {
        String[] commandsTemp = userInput.split("\n");
        StringBuilder myCommandsTemp = new StringBuilder();
        for (int i = 0; i < commandsTemp.length; ++i)
            myCommandsTemp.append(commandsTemp[i]);
        commandsTemp = myCommandsTemp.toString().split(";");
        for (int i = 0; i < commandsTemp.length; ++i)
            statementQueue.add(commandsTemp[i]);
    }

    private boolean isIfExpression(String expression, int rowNumber) {
        if (!(expression.charAt(0) == 'i' && expression.charAt(1) == 'f'))
            return false;
        int firstIndice = findFirstCharacter(expression, 2, ' ');
        if (expression.charAt(firstIndice) != '(')
            return false;
        ParenChecker pc = new ParenChecker();
        if (pc.isBalanced(expression, 2));
        String insideIf = expression.substring(firstIndice + 1, pc.getIfIndice());
        if (!isFunction(insideIf, rowNumber))
            return false;
        int begin = findIfExpression(expression, pc.getIfIndice() + 1);
        String ifExpression = expression.substring(begin);
        ifExpression = leaveSpacesInCommand(ifExpression);
        insideIf = leaveSpacesInCommand(insideIf);
        if (!isFunction(ifExpression, rowNumber))
            return false;
        addStatementAnim(insideIf);
        addStatementAnim(ifExpression);
        return true;
    }

    private int findIfExpression(String expression, int begin) {
        while (expression.charAt(begin) == ' ')
            ++begin;
        return begin;
    }

    private int findFirstChar(String expression, int indice, char ch) {
        while (expression.charAt(indice) != ch)
            ++indice;
        return indice;
    }

    private int findFirstCharacter(String expression, int indice, char ch) {
        while (expression.charAt(indice) == ch)
            ++indice;
        return indice;
    }

    private boolean isFunction(String expression, int rowNumber) {
        int retValOneCommandGood = oneCommandGood(expression);
        if (retValOneCommandGood == -1)
            allErrors.offer(new SyntaxError(errorType.WrongUsageOfOperator, rowNumber));
        if (retValOneCommandGood == 0)
            allErrors.offer(new SyntaxError(errorType.IdentifierAfterOperator, rowNumber));
        if (retValOneCommandGood == 1)
            allErrors.offer(new SyntaxError(errorType.UnexpectedIdentifier ,rowNumber));
        return retValOneCommandGood == 99;
    }

    private boolean isOperatorExist(String theCommand) {
        if (theCommand.length() < 2)
            return false;
        for (int i = 0; i + 1 < theCommand.length(); ++i) {
            if (theCommand.charAt(i) == '(' && theCommand.charAt(i + 1) == ')')
                return true;
        }
        return false;
    }

    private int oneCommandGood(String theCommand) {
        if (!isOperatorExist(theCommand))
            return -1;
        if (afterwardIdentifierExist(theCommand))
            return 0;
        StringBuilder identifier = getIdentifier(theCommand);
        if (!isIdentifierGood(identifier.toString()))
            return 1;
        return 99;
    }

    private boolean isIdentifierGood(String theIdentifier) {
        if (theIdentifier.length() == 0)
            return false;
        for (int i = 0; i < allMethodsIdentifiers.length; ++i) {
            if (theIdentifier.compareTo(allMethodsIdentifiers[i]) == 0)
                return true;
        }
        return false;
    }

    private boolean afterwardIdentifierExist(String theCommand) {
        int i;
        for (i = 0; i + 1 < theCommand.length(); ++i) {
            if (theCommand.charAt(i) == '(' && theCommand.charAt(i + 1) == ')')
                break;
        }

        if (i + 2 == theCommand.length())
            return false;
        for (int k = i + 2; k < theCommand.length(); ++k) {
            if (theCommand.charAt(k) != ' ')
                return true;
        }
        return false;
    }

    private StringBuilder getIdentifier(String theCommand) {
        StringBuilder theIdentifier = new StringBuilder();
        for (int i = findFirstIndice(theCommand); i + 1 < theCommand.length(); ++i) {
            if (theCommand.charAt(i) == '(' && theCommand.charAt(i + 1) == ')')
                return theIdentifier;
            theIdentifier.append(theCommand.charAt(i));
        }
        return theIdentifier;
    }

    private Queue<animType> handleStatementQueue() {
        Queue<animType> temp = new LinkedList<animType>();
        int size=statementQueue.size();
        for(int i=0 ; i< size ;++i){
            if(statementQueue.peek().equals("asagiAdimAt()")){
                temp.add(animType.Down);
                statementQueue.poll();
            }
            else if(statementQueue.peek().equals("yukariAdimAt()")){
                temp.add(animType.Up);
                statementQueue.poll();
            }
            else if(statementQueue.peek().equals("sagaAdimAt()")){
                temp.add(animType.Right);
                statementQueue.poll();
            }
            else if(statementQueue.peek().equals("solaAdimAt()")){
                temp.add(animType.Left);
                statementQueue.poll();
            }
            else if(statementQueue.peek().equals("engelVar()")) {
                temp.add(animType.Obstacle);
                statementQueue.poll();
            }
        }
        return temp;
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(context,message);
    }

    private ArrayList<TheObject> getObjects() {
        return objects;
    }

    /**
     * String array'i olan expression[] ' daki tum ifadelerin onundeki ve arkasindaki gereksiz bosluklari siler.
     * bosluk karakterleri ile dolu bir ifade gelirse "    " gibi, "" dondurur.
     * @param expression
     * @return
     */
    private String deleteSpacesForParsing(String expression) {
        int first, second;
        for (first = 0; first < expression.length(); ++first) {
            if (!Character.isSpaceChar(expression.charAt(first)))
                break;
        }
        for (second = expression.length() - 1; second >= 0; --second) {
            if (!Character.isSpaceChar(expression.charAt(second)))
                break;
        }
        return (first <= second) ? expression.substring(first, second + 1) : "";
    }

    private String deleteFirstSpaces(String expression) {
        int i;
        for (i = 0; i < expression.length(); ++i) {
            if (Character.isAlphabetic(expression.charAt(i)) || Character.isDigit(expression.charAt(i)))
                break;
        }
        return expression.substring(i, expression.length());
    }

    /**
     * Bosluklara gore parcalara bolunen expression'dailk kelime int degilse ya da diger kelimelrede herhangi int var ise
     * declaration degil der, bunlarin tersi ise bu satir bir deklaarationdur der.
     * @param expression
     * @return
     */
    private boolean isDeclaration(String expression) {
        String[] expressionAtoms = expression.split(" ");
        for (int i = 1; i < expressionAtoms.length; ++i) {
            if (!expressionAtoms[0].equals("int") || expressionAtoms[i].equals("int") || expressionAtoms.length < 2) {
                if (expressionAtoms.length < 2) {
                    errors.add("Degisken ismi eksik!!");
                }
                return false;
            }
        }
        return true;
    }

    private boolean isUnproperCharacterInExpression(String expression) {
        for (int i = 0; i < expression.length(); ++i) {
            if (!(Character.isAlphabetic(expression.charAt(i)) || Character.isSpaceChar(expression.charAt(i))
                    || isComma(expression.charAt(i)) || isAssignment(expression.charAt(i)) || Character.isDigit(expression.charAt(i)))) {
                errors.add("Bu ifadede, kullanimi yasak olan karakter var!!");
                return true;
            }
        }
        return false;
    }

    private boolean makeOperator(String expression) {
        int i;
        int minus = hasOperator(expression, "--");
        int plus = hasOperator(expression, "++");
        String identifier = getIdentifierWithoutSpaces(expression);
        for (i = 0; i < objects.size(); ++i) {
            if (objects.get(i).getName().equals(identifier)) {
                if (plus != -1)
                    objects.get(i).incrementData();
                else if (minus != -1)
                    objects.get(i).decrementData();
                break;
            }
        }
        if (i == objects.size()) {
            errors.add("Bu isimde bir degisken yok!!");
            return false;
        }
        return true;
    }

    private boolean isOperatorExpression(String expression) {
        int indice1 = hasOperator(expression, "++");
        int indice2 = hasOperator(expression, "--");
        if (!(indice1 != -1 || indice2 != -1)) {
            errors.add("Operator yok!!");
            return false;
        }
        return true;
    }

    private String getIdentifierWithoutSpaces(String expression) {
        StringBuilder sb = new StringBuilder(expression);
        expression = deleteSpacesForParsing(expression);
        int i;
        for (i = expression.length() - 1; i > 0; --i) {
            if (!Character.isSpaceChar(expression.charAt(i)))
                break;
        }

        return sb.substring(2, i + 1);
    }

    private int hasOperator(String expression, String operator) {
        for (int i = 0; i < expression.length() - 1; ++i) {
            if (expression.charAt(i) == operator.charAt(0) && expression.charAt(i + 1) == operator.charAt(1))
                return i + 1;
        }
        return -1;
    }

    /**
     * expression'dan int kelimesini cikarir. expression'da hangi variable'lar var onlari belirler. bu variable'lar ile
     * temporary objeler olusturur(temporay objeler ile ilgili detayli bilgi createItsTemporaryTheObject metodunda), bu objelerin
     * name ve dataString alanlarini temizler ve bunlarin analizlerini yaptirir. Ardindan da, ayni isimde birden fazla degisken
     * var mi bakar, eger var ise sondakileri siler ve bunlaarla ilgili hat yazisi olusturur.
     * @param expression
     * @return true if the expression has the keyword int, false otherwise
     */
    private void manageDeclarationProcess(String expression) {
        if (expression.length() == 0)
            return;
        StringBuilder expressionWithoutInt = new StringBuilder();
        for (int i = 3; i < expression.length(); ++i)
            expressionWithoutInt.append(expression.charAt(i));

        String[] variablesInExpression = expressionWithoutInt.toString().split(",");
        for (int i = 0; i < variablesInExpression.length; ++i)
            createItsTemporaryTheObject(variablesInExpression[i]);
        for (int i = 0; i < objects.size(); ++i)
            objects.get(i).deleteSpacesInObject();
        for (int i = 0; i < objects.size(); ++i)
            objects.get(i).dataAnalyser(objects);
        clearSameNamedObjects();
    }

    /**
     * Eğer ilkendirilmemiş obje varsa dataString'ine null atar, ilklendirilmişse dataString'ine, ilklendirildigi degeri.
     * TemporaryObject dememizin sebebi, henüz TheObject nesnesinin icindeki data degerinin doldurulmamis olmasi. DataString
     * incelenecek, sayiya cevrilebilecek turdense cevirlip data'ya atilacak, degilse onceki degisken isimlerinden birisi mi
     * bakilacak, oyleyse o degiskenin degeri atanacak, bunlar degilse pop-up cikip sentaks hatasi diyecek.
     * @param atom
     * @return
     */
    private boolean createItsTemporaryTheObject(String atom) {
        String[] atoms = atom.split("=");
        if (atoms.length == 1)
            objects.add(new TheObject(deleteFirstSpaces(atoms[0]), null));
        else
            objects.add(new TheObject(deleteFirstSpaces(atoms[0]), deleteFirstSpaces(atoms[1])));
        return true;

    }

    private void clearSameNamedObjects() {
        for (int i = 0; i < objects.size(); ++i) {
            for (int k = i + 1; k < objects.size(); ++k) {
                if (objects.get(i).getName().equals(objects.get(k).getName())) {
                    errors.add("Ayni isimde objenin birden fazla kez deklare edilmesi");
                    //System.out.println("Ayni isimde objenin birden fazla kez deklare edilmesi");
                    objects.remove(objects.get(i));
                }
            }
        }
    }

    private boolean isComma(char ch) {
        return ch == ',';
    }

    private boolean isAssignment(char ch) {
        return ch == '=';
    }

}