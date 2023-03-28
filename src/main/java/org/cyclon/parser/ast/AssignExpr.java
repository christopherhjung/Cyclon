package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AssignExpr implements Expr{
    private IdentExpr key;
    private Expr value;

    @Override
    public Expr reduce() {
        return value.reduce();
    }

    @Override
    public void bind() {
        key.assign(value);
    }
}
