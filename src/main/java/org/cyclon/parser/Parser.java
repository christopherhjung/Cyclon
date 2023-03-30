package org.cyclon.parser;

import org.cyclon.Reducer;
import org.cyclon.exception.ParseException;
import org.cyclon.lexer.Lexer;
import org.cyclon.lexer.Token;
import org.cyclon.parser.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static Expr parseRaw(String cyclon){
        var lex = new Lexer(cyclon);
        var parser = new Parser(lex);
        return parser.parse();
    }

    public static Expr parse(String cyclon){
        var lex = new Lexer(cyclon);
        var parser = new Parser(lex);
        var ast = parser.parse();
        ast.bind();

        return ast.reduce();
    }

    private final Map<String, IdentExpr> idents = new HashMap<>();
    private final Lexer lexer;
    private Token token;

    private Parser(Lexer lexer){
        this.lexer = lexer;
        next();
    }

    private Token peek(){
        return token;
    }

    private void shift(){
        token = lexer.next();
    }

    private Token next(){
        var prev = token;
        shift();
        return prev;
    }

    private boolean is(Token.Kind kind){
        return peek().getKind() == kind;
    }

    private boolean accept(Token.Kind kind){
        if(is(kind)){
            shift();
            return true;
        }

        return false;
    }

    private void expect(Token.Kind kind){
        if(!accept(kind)){
            throw new ParseException("Expected " + kind);
        }
    }

    public Expr parse(){
        var result = parseBlock();
        expect(Token.Kind.EOL);
        return result;
    }

    private IdentExpr parseIdent(boolean expect){
        if(is(Token.Kind.Ident)){
            var symbol = next().getSymbol();
            return idents.computeIfAbsent(symbol, IdentExpr::new);
        }else if(expect){
            throw new ParseException("Expected identifier");
        }

        return null;
    }

    private Expr parseBlock(){
        var exprs = new ArrayList<Expr>();

        var valid = true;
        while(!accept(Token.Kind.EOL)){
            if(!valid){
                throw new ParseException("Expected ; separator");
            }
            exprs.add(parseExpr());
            valid = false;
            while(accept(Token.Kind.Semi)){
                valid = true;
            };
        }

        return new BlockExpr(exprs.toArray(new Expr[0]));
    }

    public PairExpr parsePair(){
        var key = parsePrimaryExpr();
        expect(Token.Kind.Colon);
        var value = parsePrimaryExpr();
        return new PairExpr(key, value);
    }

    public ObjectExpr parseObject(){
        expect(Token.Kind.LeftBrace);
        var exprs = new ArrayList<PairExpr>();
        if(!accept(Token.Kind.RightBrace)){
            exprs.add(parsePair());
            while(!accept(Token.Kind.RightBrace)){
                expect(Token.Kind.Comma);
                exprs.add(parsePair());
            }
        }

        return new ObjectExpr(exprs.toArray(PairExpr[]::new));
    }

    public ListExpr parseList(){
        expect(Token.Kind.LeftBracket);
        var exprs = new ArrayList<Expr>();
        if(!accept(Token.Kind.RightBracket)){
            exprs.add(parsePrimaryExpr());
            while(!accept(Token.Kind.RightBracket)){
                expect(Token.Kind.Comma);
                exprs.add(parsePrimaryExpr());
            }
        }

        return new ListExpr(exprs.toArray(Expr[]::new));
    }

    private Expr parseValueExpr(){
        var kind = peek().getKind();
        switch (kind){
            case LeftBrace: return parseObject();
            case LeftBracket: return parseList();
        }

        var symbol = peek().getSymbol();
        Object value;
        switch (peek().getKind()){
            case String:
                value = symbol;
                break;
            case Boolean:
                value = Boolean.parseBoolean(symbol);
                break;
            case Number:
                value = Integer.parseInt(symbol);
                break;
            case Null: {
                next();
                value = null;
                break;
            }
            default: throw new ParseException("Expected Identifier, String, Boolean or Number");
        }
        next();
        return new LiteralExpr(value);
    }

    private Expr parsePrimaryExpr(){
        if(is(Token.Kind.Ident)) {
            return parseIdent(true);
        }

        return parseValueExpr();
    }

    private Expr parseExpr(){
        var ident = parseIdent(false);
        if(ident != null){
            if(accept(Token.Kind.Assign)){
                return new AssignExpr(ident, parseValueExpr());
            }

            return ident;
        }else{
            return parsePrimaryExpr();
        }
    }

}
