package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyclon.visitor.ResultVisitor;
import org.cyclon.visitor.Visitor;


@AllArgsConstructor
@Getter
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
