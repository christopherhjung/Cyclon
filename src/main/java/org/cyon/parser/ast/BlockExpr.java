package org.cyon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyon.visitor.ResultVisitor;
import org.cyon.visitor.Visitor;

@AllArgsConstructor
@Getter
public class BlockExpr implements Expr{
    private Expr[] exprs;

    @Override
    public void visit(Visitor visitor) {
        visitor.visitBlock(this);
    }

    @Override
    public <T> T visit(ResultVisitor<T> visitor) {
        return visitor.visitBlock(this);
    }
}
