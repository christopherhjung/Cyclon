package org.cyon.core.visitor;

public class IdentFactoryImpl implements IdentFactory{
    private static final char[] CHARS = new char[2 * 26 + 10];
    private static final int FIRST_WIDTH = 26;
    private static final int REMAINING_WIDTH = 26;
    static{
        for(var i = 0; i < CHARS.length ; i++ ){
            char cha;
            if(i < 26){
                cha = (char)(i + 'A');
            }else if(i < 52){
                cha = (char)(i - 26 + 'a');
            }else{
                cha = (char)(i - 52 + '0');
            }
            CHARS[i] = cha;
        }
    }

    private int counter = 0;
    private final StringBuilder sb = new StringBuilder();

    private void append(int idx){
        sb.append(CHARS[idx]);
    }

    private void next(int remainder){
        if(remainder < FIRST_WIDTH){
            append(remainder % FIRST_WIDTH);
        }else{
            next(remainder / REMAINING_WIDTH);
            append(remainder % REMAINING_WIDTH);
        }
    }

    @Override
    public String next() {
        sb.setLength(0);
        next(counter++);
        return sb.toString();
    }
}
