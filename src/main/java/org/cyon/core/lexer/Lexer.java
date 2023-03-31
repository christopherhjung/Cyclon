package org.cyon.core.lexer;

import org.apache.commons.lang3.CharUtils;


public class Lexer {
    private final char[] chars;
    private int idx;
    private int mark = 0;
    private Token.Enter enter;

    public Lexer(String code){
        this.chars = code.toCharArray();
    }

    private int curr(){
        return isEOL() ? -1 : chars[idx];
    }

    private char currChar(){
        assert !isEOL();
        return chars[idx];
    }

    private char eat(){
        assert !isEOL();
        var result =  chars[idx];
        shift();
        return result;
    }

    private void shift(){
        idx++;
    }

    private boolean isEOL(){
        return idx >= chars.length;
    }

    public int ahead(int offset){
        if(idx + offset + 1 >= chars.length){
            return -1;
        }
        return chars[idx];
    }

    public boolean is(int cha, int offset){
        return ahead(offset) == cha;
    }

    public boolean accept(int cha){
        if(curr() == cha){
            idx++;
            return true;
        }

        return false;
    }

    private boolean isNumeric(){
        var curr = this.curr();
        if(curr == -1){
            return false;
        }else{
            return CharUtils.isAsciiNumeric((char) curr);
        }
    }

    private boolean isAlpha(){
        var curr = this.curr();
        if(curr == -1){
            return false;
        }else{
            return CharUtils.isAsciiAlpha((char) curr);
        }
    }

    private boolean isAlphaNumeric(){
        var curr = this.curr();
        if(curr == -1){
            return false;
        }else{
            return CharUtils.isAsciiAlphanumeric((char) curr);
        }
    }

    private void mark(){
        mark = idx;
    }

    private String getString(){
        return getString(idx - mark);
    }

    private String getString(int length){
        return new String(chars, mark, length);
    }

    private Token token(Token.Kind kind){
        return token(kind, null);
    }
    
    private Token token(Token.Kind kind, String symbol){
        return new Token(kind, enter, symbol);
    }

    private Token.Enter acceptWhitespace(){
        var enter = Token.Enter.Token;
        while(true){
            if(accept(' ') || accept('\t')){
                if(enter == Token.Enter.Token){
                    enter = Token.Enter.Space;
                }
            }else if(accept('\n') || accept('\r')){
                enter = Token.Enter.NL;
            }else{
                break;
            }
        }
        return enter;
    }

    public Token next(){
        while(!isEOL()){
            enter = acceptWhitespace();

            if(accept('=')){
                return token(Token.Kind.Assign);
            }

            if(accept('{')){
                return token(Token.Kind.LeftBrace);
            }

            if(accept('}')){
                return token(Token.Kind.RightBrace);
            }

            if(accept('[')){
                return token(Token.Kind.LeftBracket);
            }

            if(accept(']')){
                return token(Token.Kind.RightBracket);
            }

            if(accept(',')){
                return token(Token.Kind.Comma);
            }

            if(accept(';')){
                return token(Token.Kind.Semi);
            }

            if(accept(':')){
                return token(Token.Kind.Colon);
            }

            if( accept('/') ){
                if(accept('*') ){ // arbitrary comment
                    var depth = 1;
                    while(true) {
                        if(isEOL()){
                            return token(Token.Kind.Error);
                        }

                        if(accept('/')){
                            if(accept('*')){
                                depth += 1;
                            }
                        } else if(accept('*')){
                            if(accept('/')){
                                depth -= 1;
                                if(depth == 0 ){
                                    break;
                                }
                            }

                            continue;
                        }

                        next();
                    }
                    continue;
                }
                if(accept('/')) {
                    while(true) {
                        if(isEOL()){
                            return token(Token.Kind.Error);
                        }

                        if(accept('\n')){
                            break;
                        } else {
                            next();
                        }
                    }
                    continue;
                }
                return token(Token.Kind.Error);
            }

            if(isNumeric()){
                mark();
                shift();

                while(isNumeric()){
                    shift();
                }

                var kind = Token.Kind.Number;
                if(accept('.')){
                    kind = Token.Kind.Decimal;
                    while(isNumeric()){
                        shift();
                    }
                }

                return token(kind, getString());
            }

            if(isAlpha()){
                mark();
                shift();

                while(isAlphaNumeric()){
                    shift();
                }

                var value = getString();
                switch (value) {
                    case "true":
                    case "false": return token(Token.Kind.Boolean, value);
                    case "null": return token(Token.Kind.Null);
                }

                return token(Token.Kind.Ident, value);
            }

            if(accept('\"')){
                mark();
                while(!accept('\"')){
                    if(isEOL()){
                        return token(Token.Kind.Error);
                    }
                    shift();
                }

                return token(Token.Kind.String, getString(idx - mark - 1 ));
            }

            if(isEOL()){
                return token(Token.Kind.EOL);
            }

            return token(Token.Kind.Error);
        }

        return token(Token.Kind.EOL);
    }
}













