package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyclon.ResultVisitor;
import org.cyclon.Visitor;
import org.cyclon.mapper.Context;
import org.cyclon.mapper.Unbinder;

@AllArgsConstructor
@Getter
public class LiteralExpr implements Expr{
    private final Object value;

    @Override
    public Expr reduce() {
        return this;
    }

    @Override
    public Expr unbind(Unbinder unbinder) {
        return this;
    }

    @Override
    public Expr expand(Unbinder unbinder) {
        return this;
    }

    @Override
    public <T> T deserialize(Class<T> clazz, Context context) {
        return (T)value;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitLiteral(this);
    }

    @Override
    public <T> T visit(ResultVisitor<T> visitor) {
        return visitor.visitLiteral(this);
    }
}
