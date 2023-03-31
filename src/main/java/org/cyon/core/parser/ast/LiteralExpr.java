package org.cyon.core.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyon.core.visitor.ResultVisitor;
import org.cyon.core.visitor.Visitor;

@AllArgsConstructor
@Getter
public class LiteralExpr implements Expr{
    private final Object value;

    @Override
    public void visit(Visitor visitor) {
        visitor.visitLiteral(this);
    }

    @Override
    public <T> T visit(ResultVisitor<T> visitor) {
        return visitor.visitLiteral(this);
    }
}
