package org.cyon.core.parser;

import org.cyon.core.exception.ParseException;
import org.cyon.core.lexer.Lexer;
import org.cyon.core.lexer.Token;
import org.cyon.core.parser.ast.*;
import org.cyon.core.visitor.Binder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    public static Expr parseRaw(String str){
        var lex = new Lexer(str);
        var parser = new Parser(lex);
        return parser.parse();
    }

    public static Expr parse(String str){
        var lex = new Lexer(str);
        var parser = new Parser(lex);
        var ast = parser.parse();
        var binder = new Binder();
        binder.bind(ast);
        return ast.reduce();
    }

    private final Map<String, IdentExpr> idents = new HashMap<>();
    private final Lexer lexer;
    private Token.Kind token;

    private Parser(Lexer lexer){
        this.lexer = lexer;
        shift();
    }

    private void shift(){
        token = lexer.next();
    }

    private boolean is(Token.Kind kind){
        return token == kind;
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

    private boolean acceptOrNl(Token.Kind kind){
        return accept(kind) || lexer.getEnter() == Token.Enter.NL;
    }

    private void expectOrNl(Token.Kind kind){
        if(!acceptOrNl(kind)){
            throw new ParseException("Expected " + kind + " nr new line");
        }
    }

    private IdentExpr parseIdent(boolean expect){
        if(is(Token.Kind.Ident)){
            var symbol = lexer.getSymbol();
            shift();
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
            if(!valid) throw new ParseException("Expected ; separator");
            exprs.add(parseExpr());
            valid = accept(Token.Kind.Semi);
        }

        return new BlockExpr(exprs.toArray(Expr.EMPTY_ARRAY));
    }

    public PairExpr parsePair(){
        var key = parsePrimaryExpr();
        expect(Token.Kind.Colon);
        var value = parsePrimaryExpr();
        return new PairExpr(key, value);
    }

    public ObjectExpr parseObject(){
        expect(Token.Kind.LeftBrace);

        if(accept(Token.Kind.RightBrace)){
            return new ObjectExpr(PairExpr.EMPTY_ARRAY);
        }

        var first = parsePair();
        if(accept(Token.Kind.RightBrace)){
            return new ObjectExpr(new PairExpr[]{first});
        }

        var exprs = new ArrayList<PairExpr>();
        exprs.add(first);
        do{
            expect(Token.Kind.Comma);
            exprs.add(parsePair());
        }while(!accept(Token.Kind.RightBrace));

        return new ObjectExpr(exprs.toArray(PairExpr.EMPTY_ARRAY));
    }

    public ListExpr parseList(){
        expect(Token.Kind.LeftBracket);
        if(accept(Token.Kind.RightBracket)){
            return new ListExpr(Expr.EMPTY_ARRAY);
        }

        var first = parsePrimaryExpr();
        if(accept(Token.Kind.RightBracket)){
            return new ListExpr(new Expr[]{first});
        }

        var exprs = new ArrayList<Expr>();
        exprs.add(first);
        do{
            expect(Token.Kind.Comma);
            exprs.add(parsePrimaryExpr());
        }while(!accept(Token.Kind.RightBracket));
        return new ListExpr(exprs.toArray(Expr.EMPTY_ARRAY));
    }

    private Expr parseValueExpr(){
        switch (token){
            case LeftBrace: return parseObject();
            case LeftBracket: return parseList();
        }

        var symbol = lexer.getSymbol();
        Object value;
        switch (token){
            case String:
                value = symbol;
                break;
            case Boolean:
                value = Boolean.parseBoolean(symbol);
                break;
            case Number:
                value = Integer.parseInt(symbol);
                break;
            case Decimal:
                value = Double.parseDouble(symbol);
                break;
            case Null: {
                value = null;
                break;
            }
            default:
                throw new ParseException("Expected Identifier, String, Boolean, Number or Null!");
        }
        shift();
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
