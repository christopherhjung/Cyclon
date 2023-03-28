package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class PairExpr implements Expr{
    private Expr key;
    private Expr value;

    @Override
    public Expr reduce() {
        key = key.reduce();
        value = value.reduce();
        return this;
    }
}
