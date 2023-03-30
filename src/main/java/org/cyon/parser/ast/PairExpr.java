package org.cyon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.cyon.visitor.ResultVisitor;
import org.cyon.visitor.Visitor;

@AllArgsConstructor
@Getter
@Setter
public class PairExpr implements Expr{
    private Expr key;
    private Expr value;

    @Override
    public void visit(Visitor visitor) {
        visitor.visitPair(this);
    }

    @Override
    public <T> T visit(ResultVisitor<T> visitor) {
        return visitor.visitPair(this);
    }
}
