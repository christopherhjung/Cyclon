package org.cyon.core.lexer;


public class Lexer {
    private final char[] chars;
    private int idx = -1;
    private int mark = 0;
    private int curr;
    private Token.Enter enter;
    private String symbol;

    public Lexer(String code){
        this.chars = code.toCharArray();
        shift();
    }

    public String getSymbol() {
        return symbol;
    }

    private void shift(){
        curr = ++idx >= chars.length ? -1 : chars[idx];
    }

    private boolean eol(){
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
        return new String(chars, mark, idx - mark);
    }

    private Token token(Token.Kind kind){
        return token(kind, null);
    }
    
    private Token token(Token.Kind kind, String symbol){
        return new Token(kind, enter, symbol);
    }

    private Token.Enter acceptWhitespace(){
        var enter = Token.Enter.Token;
        loop: while(true){
            switch (curr){
                case ' ':
                case '\t':
                    shift();
                    enter = Token.Enter.Space;
                    break;
                case '\n':
                case '\r':
                    shift();
                    enter = Token.Enter.NL;
                    break loop;
                default:
                    return enter;
            }
        }

        while(true){
            switch (curr){
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    shift();
                    break;
                default:
                    return enter;
            }
        }
    }

    private Token.Kind parseSign(){
        switch (curr){
            case '=': shift(); return Token.Kind.Assign;
            case '{': shift(); return Token.Kind.LeftBrace;
            case '}': shift(); return Token.Kind.RightBrace;
            case '[': shift(); return Token.Kind.LeftBracket;
            case ']': shift(); return Token.Kind.RightBracket;
            case ',': shift(); return Token.Kind.Comma;
            case ';': shift(); return Token.Kind.Semi;
            case ':': shift(); return Token.Kind.Colon;
            case '\"': return acceptString();
            default: return null;
        }
    }

    public Token.Kind next(){
        while(!eol()){
            enter = acceptWhitespace();

            var sign = parseSign();
            if(sign != null) return sign;

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

                symbol = getString();
                return kind;
            }

            if(isAlpha()){
                mark();
                shift();

                while(isAlphaNumeric()){
                    shift();
                }

                symbol = getString();
                switch (symbol) {
                    case "true":
                    case "false": return Token.Kind.Boolean;
                    case "null": return Token.Kind.Null;
                }

                return Token.Kind.Ident;
            }

            if( accept('/') ){
                var comment = acceptComment();
                if(comment == Token.Kind.Comment){
                    continue;
                }
                return comment;
            }

            if(eol()){
                return Token.Kind.EOL;
            }

            return Token.Kind.Error;
        }

        return Token.Kind.EOL;
    }

    private Token.Kind acceptComment(){
        if(accept('*') ){ // arbitrary comment
            var depth = 1;
            while(true) {
                if(eol()) return Token.Kind.Error;

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
                }else{
                    shift();
                }
            }
            return Token.Kind.Comment;
        }

        if(accept('/')) {
            while(!accept('\n')) {
                if(eol()) return Token.Kind.Error;
                shift();
            }

            return Token.Kind.Comment;
        }

        return Token.Kind.Error;
    }

    private Token.Kind acceptString(){
        shift();
        mark();
        loop: while(true){
            switch (curr){
                case '\"': break loop;
                case '\\': return acceptEscapingString();
                case -1: return Token.Kind.Error;
            }
            shift();
        }

        symbol = getString();
        shift();
        return Token.Kind.String;
    }

    private Token.Kind acceptEscapingString(){
        var sb = new StringBuilder();
        loop: while(true){
            switch (curr){
                case '\"': break loop;
                case '\\':
                    sb.append(getString());
                    shift();
                    mark();
                    break;
                case -1: return Token.Kind.Error;
            }

            shift();
        }

        sb.append(getString());
        shift();
        symbol = sb.toString();
        return Token.Kind.String;
    }
}













