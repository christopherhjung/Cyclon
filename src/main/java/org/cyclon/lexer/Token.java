package org.cyclon.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Token {
    private Kind kind;
    private Enter enter;
    private String symbol;

    public enum Kind{
        Ident, String, Boolean, Number, Null,
        Assign,
        LeftBracket, RightBracket,
        LeftBrace, RightBrace,
        Comma, Semi, Colon,
        EOL, Error
    }

    public enum Enter{
        Token, Space, NL
    }
}
