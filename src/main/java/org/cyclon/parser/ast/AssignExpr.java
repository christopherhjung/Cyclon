package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AssignExpr implements Expr{
    private IdentExpr key;
    private Expr value;

    @Override
    public Expr bind() {
        return value.bind();
    }

    @Override
    public void scan() {
        key.assign(value);
    }
}
