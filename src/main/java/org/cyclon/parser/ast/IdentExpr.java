package org.cyclon.parser.ast;

import lombok.Getter;
import lombok.Setter;
import org.cyclon.Visitor;
import org.cyclon.exception.ParseException;

@Getter
@Setter
public class IdentExpr implements Expr{
    private String key;
    private Expr expr;

    public IdentExpr(){

    }

    public IdentExpr(String key){
        this.key = key;
    }

    @Override
    public Expr reduce() {
        return expr == null ? this : expr;
    }

    public void assign(Expr value, boolean check){
        if(expr != null && check){
            throw new ParseException("Double assign");
        }
        this.expr = value;
    }

    public void toggle(Expr value){
        expr = expr == null ? value : null;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitIdent(this);
    }
}
