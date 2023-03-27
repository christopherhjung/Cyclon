package org.cyclon.parser.ast;

import lombok.RequiredArgsConstructor;
import org.cyclon.exception.ParseException;

@RequiredArgsConstructor
public class IdentExpr implements Expr{
    private final String key;
    private Expr expr;

    @Override
    public Expr bind() {
        if(expr == null){
            throw new ParseException("Binding failed");
        }
        return expr;
    }

    public void assign(Expr value){
        if(expr != null){
            throw new ParseException("Double assign");
        }
        this.expr = value;
    }
}
