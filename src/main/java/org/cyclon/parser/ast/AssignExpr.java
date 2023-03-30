package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.cyclon.visitor.ResultVisitor;
import org.cyclon.visitor.Visitor;

@AllArgsConstructor
@Getter
@Setter
public class AssignExpr implements Expr{
    private IdentExpr key;
    private Expr value;

    @Override
    public void visit(Visitor visitor) {
        visitor.visitAssign(this);
    }

    @Override
    public <T> T visit(ResultVisitor<T> visitor) {
        return visitor.visitAssign(this);
    }
}
