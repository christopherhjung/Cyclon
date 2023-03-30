package org.cyon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.cyon.visitor.ResultVisitor;
import org.cyon.visitor.Visitor;


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
