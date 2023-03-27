package org.cyclon;

import org.cyclon.lexer.Lexer;
import org.cyclon.lexer.Token;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LexerTest {
    @Test
    public void simpleLexTest(){
        var lexer = new Lexer("a = \"2\"; b = 3");

        var token1 = lexer.next();
        Assert.assertEquals(Token.Kind.Ident, token1.getKind());
        assertEquals("a", token1.getSymbol());

        var token2 = lexer.next();
        assertEquals(Token.Kind.Assign, token2.getKind());
        assertNull(token2.getSymbol());

        var token3 = lexer.next();
        assertEquals(Token.Kind.String, token3.getKind());
        assertEquals("2", token3.getSymbol());

        var token4 = lexer.next();
        assertEquals(Token.Kind.Semi, token4.getKind());
        assertNull(token4.getSymbol());

        var token5 = lexer.next();
        assertEquals(Token.Kind.Ident, token5.getKind());
        assertEquals("b", token5.getSymbol());

        var token6 = lexer.next();
        assertEquals(Token.Kind.Assign, token6.getKind());
        assertNull(token6.getSymbol());

        var token7 = lexer.next();
        assertEquals(Token.Kind.Number, token7.getKind());
        assertEquals("3", token7.getSymbol());
    }
}
