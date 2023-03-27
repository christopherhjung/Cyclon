package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class PairExpr implements Expr{
    private Expr key;
    private Expr value;

    @Override
    public Expr bind() {
        key = key.bind();
        value = value.bind();
        return this;
    }
}
