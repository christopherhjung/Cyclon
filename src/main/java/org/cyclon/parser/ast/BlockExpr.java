package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlockExpr implements Expr{
    private Expr[] exprs;

    @Override
    public Expr bind() {
        Expr last = null;
        for(var elem : exprs){
            last = elem.bind();
        }
        return last;
    }

    @Override
    public void scan() {
        for(var elem : exprs){
            elem.scan();
        }
    }
}
