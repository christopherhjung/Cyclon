package org.cyon.core.visitor;

public class AlphaNumericIdentSequence implements IdentSequence {
    private static final char[] CHARS = new char[62];
    private static final int FIRST_WIDTH;
    static{
        var idx = 0;
        for(; idx < CHARS.length ; idx++ ){
            char cha;
            if(idx < 10){
                cha = (char)(idx + '0');
            }else if(idx < 36){
                cha = (char)(idx - 10 + 'a');
            }else{
                cha = (char)(idx - 36 + 'A');
            }
            CHARS[idx] = cha;
        }
        FIRST_WIDTH = idx;
    }

    private int counter = 0;
    private final StringBuilder sb = new StringBuilder();

    private void append(int idx){
        sb.append(CHARS[idx]);
    }

    private void nextFirst(int remainder){
        if(remainder < FIRST_WIDTH){
            append(remainder);
        }else{
            next(remainder / FIRST_WIDTH);
            append(remainder % FIRST_WIDTH);
        }
    }

    private void next(int remainder){
        if(remainder < (FIRST_WIDTH + 1)){
            append(remainder - 1);
        }else{
            var test = remainder - (FIRST_WIDTH + 1);
            var test2 = test % FIRST_WIDTH;
            next((remainder - test2) / FIRST_WIDTH);
            append(test2);
        }
    }

    @Override
    public String next() {
        sb.setLength(0);
        nextFirst(counter++);
        return sb.toString();
    }
}
