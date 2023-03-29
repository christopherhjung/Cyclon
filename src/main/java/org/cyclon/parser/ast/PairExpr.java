package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyclon.mapper.Unbinder;


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

    @Override
    public Expr expand(Unbinder unbinder) {
        key = key.unbind(unbinder);
        value = value.unbind(unbinder);
        return this;
    }
}
