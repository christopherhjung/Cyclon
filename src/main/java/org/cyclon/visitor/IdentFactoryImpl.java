package org.cyclon.visitor;

public class IdentFactoryImpl implements IdentFactory{
    private static final char[] CHARS = new char[2 * 26 + 10];
    static{
        for(var i = 0; i < CHARS.length ; i++ ){
            char cha;
            if(i < 26){
                cha = (char)(i + 'a');
            }else if(i < 52){
                cha = (char)(i - 26 + 'A');
            }else{
                cha = (char)(i - 36 + '0');
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
        if(remainder < 52){
            append(remainder % 52);
        }else{
            next(remainder / 62);
            append(remainder % 62);
        }
    }

    @Override
    public String next() {
        sb.setLength(0);
        next(counter++);
        return sb.toString();
    }
}
