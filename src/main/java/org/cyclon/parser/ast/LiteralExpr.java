package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyclon.visitor.ResultVisitor;
import org.cyclon.visitor.Visitor;
import org.cyclon.mapper.Context;

@AllArgsConstructor
@Getter
public class LiteralExpr implements Expr{
    private final Object value;

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
