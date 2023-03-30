package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.cyclon.ResultVisitor;
import org.cyclon.Visitor;
import org.cyclon.mapper.Unbinder;


@AllArgsConstructor
@Getter
@Setter
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

    @Override
    public void visit(Visitor visitor) {
        visitor.visitPair(this);
    }

    @Override
    public <T> T visit(ResultVisitor<T> visitor) {
        return visitor.visitPair(this);
    }
}
