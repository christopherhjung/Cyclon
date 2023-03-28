package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlockExpr implements Expr{
    private Expr[] exprs;

    @Override
    public Expr reduce() {
        Expr last = null;
        for(var elem : exprs){
            last = elem.reduce();
        }
        return last;
    }

    @Override
    public void bind() {
        for(var elem : exprs){
            elem.bind();
        }
    }
}
