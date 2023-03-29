package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.cyclon.Visitor;

@AllArgsConstructor
@Getter
@Setter
public class AssignExpr implements Expr{
    private IdentExpr key;
    private Expr value;

    @Override
    public Expr reduce() {
        return value.reduce();
    }

    @Override
    public void bind() {
        key.assign(value, true);
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitAssign(this);
    }
}
