package org.cyclon.parser.ast;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.cyclon.exception.ParseException;
import org.cyclon.mapper.Unbinder;

@RequiredArgsConstructor
public class IdentExpr implements Expr{
    private final String key;
    private Expr expr;

    @Override
    public Expr reduce() {
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

    @Override
    public Expr unbind(Unbinder unbinder) {
        throw new NotImplementedException("Not impl");
    }
}
