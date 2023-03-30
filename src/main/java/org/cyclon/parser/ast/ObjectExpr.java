package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyclon.visitor.ResultVisitor;
import org.cyclon.visitor.Visitor;
import org.cyclon.mapper.Context;

import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@Getter
public class ObjectExpr implements Expr{
    private PairExpr[] pairs;

    @Override
    public void visit(Visitor visitor) {
        visitor.visitObject(this);
    }

    @Override
    public <T> T visit(ResultVisitor<T> visitor) {
        return visitor.visitObject(this);
    }
}
