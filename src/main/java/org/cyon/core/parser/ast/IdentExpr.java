package org.cyon.core.parser.ast;

import lombok.Getter;
import lombok.Setter;
import org.cyon.core.exception.ParseException;
import org.cyon.core.visitor.ResultVisitor;
import org.cyon.core.visitor.Visitor;

@Getter
@Setter
public class IdentExpr extends Expr{
    private String key;

    public IdentExpr(){}

    public IdentExpr(String key){
        this.key = key;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitIdent(this);
    }

    @Override
    public <T> T visit(ResultVisitor<T> visitor) {
        return visitor.visitIdent(this);
    }
}
