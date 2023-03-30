package org.cyclon.parser.ast;

import org.apache.commons.lang3.NotImplementedException;
import org.cyclon.visitor.Expander;
import org.cyclon.visitor.Reducer;
import org.cyclon.visitor.ResultVisitor;
import org.cyclon.visitor.Visitor;
import org.cyclon.mapper.Context;

public interface Expr {
    void visit(Visitor visitor);
    <T> T visit(ResultVisitor<T> visitor);
    default void bind(){}
    default Expr expand(){
        var expander = new Expander();
        return expander.expand(this);
    }
    default Expr reduce(){
        var reducer = new Reducer();
        return reducer.reduce(this);
    };
    default <T> T deserialize(Class<T> clazz, Context context){
        throw new NotImplementedException("Not implemented!");
    }
    default IdentExpr serialize(StringBuilder sb, Context context){
        throw new NotImplementedException("Not implemented!");
    }
}
