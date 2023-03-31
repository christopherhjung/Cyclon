package org.cyon.core.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.cyon.core.visitor.ResultVisitor;
import org.cyon.core.visitor.Visitor;


@AllArgsConstructor
@Getter
@Setter
public class ListExpr implements Expr{
    private Expr[] elems;

    @Override
    public void visit(Visitor visitor) {
        visitor.visitList(this);
    }

    @Override
    public <T> T visit(ResultVisitor<T> visitor) {
        return visitor.visitList(this);
    }
}
