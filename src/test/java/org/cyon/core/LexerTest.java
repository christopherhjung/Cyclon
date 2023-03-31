package org.cyon.core;

import org.cyon.core.lexer.Lexer;
import org.cyon.core.lexer.Token;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LexerTest {
    @Test
    public void simpleLexTest(){
        var lexer = new Lexer("a = \"2\"; b = 3");

        var token1 = lexer.next();
        Assert.assertEquals(Token.Kind.Ident, token1);
        assertEquals("a", lexer.getSymbol());

        var token2 = lexer.next();
        assertEquals(Token.Kind.Assign, token2);

        var token3 = lexer.next();
        assertEquals(Token.Kind.String, token3);
        assertEquals("2", lexer.getSymbol());

        var token4 = lexer.next();
        assertEquals(Token.Kind.Semi, token4);

        var token5 = lexer.next();
        assertEquals(Token.Kind.Ident, token5);
        assertEquals("b", lexer.getSymbol());

        var token6 = lexer.next();
        assertEquals(Token.Kind.Assign, token6);

        var token7 = lexer.next();
        assertEquals(Token.Kind.Number, token7);
        assertEquals("3", lexer.getSymbol());
    }
}
