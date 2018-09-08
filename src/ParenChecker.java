import java.util.Stack;
import java.util.EmptyStackException;
import javax.swing.JOptionPane;

/** Class to check for balanced parentheses.
 *   @author Koffman & Wolfgang
 * */

public class ParenChecker {

    // Constants
    /** Set of opening parenthesis characters. */
    private static final String OPEN = "(";

    /** Set of closing parenthesis characters, matches OPEN. */
    private static final String CLOSE = ")";

    private boolean firstBalanceIndice = false;
    private int ifIndice = 0;

    /** Test the input string to see that it contains balanced
     parentheses. This method tests an input string to see
     that each type of parenthesis is balanced. '(' is matched
     with ')', '[' is matched with ']', and
     '{' is matched with '}'.
     @param expression A String containing the expression to
     be examined
     @return true if all the parentheses match
     */
    public boolean isBalanced(String expression, int index) {
        // Create an empty stack.
        Stack < Character > s = new Stack < Character > ();
        boolean balanced = true;
        boolean flag = false;
        try {
            while (balanced && index < expression.length()) {
                char nextCh = expression.charAt(index);
                if (isOpen(nextCh)) {
                    s.push(nextCh);
                }
                else if (isClose(nextCh)) {
                    char topCh = s.pop();
                    balanced = OPEN.indexOf(topCh)
                            == CLOSE.indexOf(nextCh);
                }
                if (s.size() != 0)
                    firstBalanceIndice = true;
                if (s.size() == 0 && firstBalanceIndice && !flag) {
                    ifIndice = index;
                    firstBalanceIndice = false;
                    flag = true;
                }
                index++;
            }
        }
        catch (EmptyStackException ex) {
            balanced = false;
        }
        return (balanced && s.empty());
    }

    /** Method to determine whether a character is one of the
     opening parentheses.
     @param ch Character to be tested
     @return true if ch is one of the opening parentheses
     */
    private static boolean isOpen(char ch) {
        return OPEN.indexOf(ch) > -1;
    }

    /** Method to determine whether a character is one of the
     closing parentheses.
     @param ch Character to be tested
     @return true if ch is one of the closing parentheses
     */
    private static boolean isClose(char ch) {
        return CLOSE.indexOf(ch) > -1;
    }

    public int getIfIndice() {
        return ifIndice;
    }

}

