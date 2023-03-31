package org.cyon.core.lexer;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;


public class Lexer {
    private final char[] chars;
    private int idx = -1;
    private int mark = 0;
    private int curr;
    private Token.Enter enter;

    public Lexer(String code){
        this.chars = code.toCharArray();
        shift();
    }

    private void shift(){
        curr = ++idx >= chars.length ? -1 : chars[idx];
    }

    private boolean isEOL(){
        return idx >= chars.length;
    }

    public boolean accept(int cha){
        if(curr == cha){
            shift();
            return true;
        }

        return false;
    }

    private boolean isNumeric(){
        return '0' <= curr && curr <= '9';
    }

    private boolean isAlpha(){
        return 'a' <= curr && curr <= 'z' || 'A' <= curr && curr <= 'Z';
    }

    private boolean isAlphaNumeric(){
        return isAlpha() || isNumeric();
    }

    private void mark(){
        mark = idx;
    }

    private String getString(){
        return getString(0);
    }

    private String getString(int offset){
        return new String(chars, mark, idx - mark - offset);
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

    private Token.Kind parseSign(){
        var sign = parseSignImpl();
        if(sign != null){
            shift();
        }
        return sign;
    }

    private Token.Kind parseSignImpl(){
        switch (curr){
            case '=': return Token.Kind.Assign;
            case '{': return Token.Kind.LeftBrace;
            case '}': return Token.Kind.RightBrace;
            case '[': return Token.Kind.LeftBracket;
            case ']': return Token.Kind.RightBracket;
            case ',': return Token.Kind.Comma;
            case ';': return Token.Kind.Semi;
            case ':': return Token.Kind.Colon;
            default: return null;
        }
    }

    public Token next(){
        while(!isEOL()){
            enter = acceptWhitespace();

            var sign = parseSign();
            if(sign != null) return token(sign);

            if(accept('\"')){
                return acceptString();
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

            if(isEOL()){
                return token(Token.Kind.EOL);
            }

            return token(Token.Kind.Error);
        }

        return token(Token.Kind.EOL);
    }

    private Token acceptString(){
        mark();
        while(!accept('\"')){
            if(isEOL()) return token(Token.Kind.Error);
            if(accept('\\')) return acceptEscapingString();

            shift();
        }

        return token(Token.Kind.String, getString(1 ));
    }

    private Token acceptEscapingString(){
        var sb = new StringBuilder(getString(1 ));

        mark();
        shift();
        while(!accept('\"')){
            if(isEOL()) return token(Token.Kind.Error);
            if(accept('\\')){
                sb.append(getString(1 ));
                mark();
            }

            shift();
        }

        sb.append(getString(1 ));
        return token(Token.Kind.String, sb.toString());
    }
}













