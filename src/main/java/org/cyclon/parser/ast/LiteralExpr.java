package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyclon.mapper.Context;

@AllArgsConstructor
@Getter
public class LiteralExpr implements Expr{
    private final Object value;

    @Override
    public Expr reduce() {
        return this;
    }

    @Override
    public <T> T deserialize(Class<T> clazz, Context context) {
        return (T)value;
    }
}
