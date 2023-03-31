package org.cyon.core;

import org.cyon.core.exception.ParseException;
import org.cyon.core.parser.Parser;
import org.cyon.core.parser.ast.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class ParserTest {

    @Test
    public void simpleParserTest(){
        var actualExpr = Parser.parseRaw("a = \"2\"; b = 3");
        var expectedExpr = block(
            assign(id("a"), str("2")),
            assign(id("b"), number(3))
        );
        assertIdentical(expectedExpr, actualExpr);
    }

    @Test
    public void missingSemi(){
        assertThrows(ParseException.class, () -> {
            Parser.parseRaw("a = \"2\" b = 3");
        });
    }

    @Test
    public void arrayTest(){
        var actualExpr = Parser.parseRaw("a = [1, 2, 3]");
        var expectedExpr = block(
            assign(id("a"), list(number(1), number(2), number(3)))
        );
        assertIdentical(expectedExpr, actualExpr);
    }

    @Test
    public void arrayIdent(){
        var actualExpr = Parser.parseRaw("b = 9; a = [b, 3]");
        var expectedExpr = block(
            assign(id("b"), number(9)),
            assign(id("a"), list(id("b"), number(3)))
        );
        assertIdentical(expectedExpr, actualExpr);
    }

    @Test
    public void arrayRecursive(){
        var actualExpr = Parser.parseRaw("a = [a, 3]");
        var expectedExpr = block(
            assign(id("a"), list(id("a"), number(3)))
        );
        assertIdentical(expectedExpr, actualExpr);
    }

    @Test
    public void objTest(){
        var actualExpr = Parser.parseRaw("a = {\"key\": \"value\"}");
        var expectedExpr = block(
            assign(id("a"), obj(
                pair(str("key"), str("value"))
            ))
        );
        assertIdentical(expectedExpr, actualExpr);
    }

    @Test
    public void objKeyAsIdentifier(){
        var actualExpr = Parser.parseRaw("key = \"key\"; a = {key: \"value\"}");
        var expectedExpr = block(
            assign(id("key"), str("key")),
            assign(id("a"), obj(
                pair(id("key"), str("value"))
            ))
        );
        assertIdentical(expectedExpr, actualExpr);
    }


    private IdentExpr id(String name){
        return new IdentExpr(name);
    }

    private Expr str(String name){
        return new LiteralExpr(name);
    }

    private Expr number(int number){
        return new LiteralExpr(number);
    }

    private Expr assign(IdentExpr lhs, Expr rhs){
        return new AssignExpr(lhs, rhs);
    }

    private Expr block(Expr... expr){
        return new BlockExpr(expr);
    }

    private Expr list(Expr... expr){
        return new ListExpr(expr);
    }

    private Expr obj(PairExpr... expr){
        return new ObjectExpr(expr);
    }

    private PairExpr pair(Expr key, Expr value){
        return new PairExpr(key, value);
    }

    private void assertIdentical(Object expected, Object actual){
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
