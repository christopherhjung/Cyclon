package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyclon.ResultVisitor;
import org.cyclon.Visitor;

@AllArgsConstructor
@Getter
public class BlockExpr implements Expr{
    private Expr[] exprs;

    @Override
    public Expr reduce() {
        Expr last = null;
        for(var elem : exprs){
            last = elem.reduce();
        }
        return last;
    }

    @Override
    public void bind() {
        for(var elem : exprs){
            elem.bind();
        }
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitBlock(this);
    }

    @Override
    public <T> T visit(ResultVisitor<T> visitor) {
        return visitor.visitBlock(this);
    }
}
