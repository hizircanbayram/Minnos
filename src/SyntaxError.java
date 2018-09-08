public class SyntaxError {

    private int rowNumber;
    private errorType theError;

    public SyntaxError(errorType theError, int rowNumber) {
        this.rowNumber = rowNumber;
        this.theError = theError;
    }

    @Override
    public String toString() {
        if (theError == errorType.IdentifierAfterOperator)
            return "anaDosya: hata  :   " + rowNumber + ". satir :: Operatorden sonra nesne ismi\n";
        else if (theError == errorType.UnexpectedIdentifier)
            return "anaDosya: hata  :" + rowNumber + ". satir :: Uygun olmayan fonksiyon ismi\n";
        else if (theError == errorType.WrongUsageOfOperator)
            return "anaDosya: hata  :" + rowNumber + ". satir :: Operatorun yanlis kullanimi\n";
        else //ileride baska hatalar eklenir diye son kosulu else if ile yaptim. 3 kosul varken buraya girmesi imkansiz!!!
            return null;
    }
}
