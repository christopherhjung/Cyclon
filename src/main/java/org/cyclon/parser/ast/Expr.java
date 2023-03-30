package org.cyclon.parser.ast;

import org.apache.commons.lang3.NotImplementedException;
import org.cyclon.Reducer;
import org.cyclon.ResultVisitor;
import org.cyclon.Visitor;
import org.cyclon.mapper.Context;
import org.cyclon.mapper.Unbinder;

public interface Expr {
    void visit(Visitor visitor);
    <T> T visit(ResultVisitor<T> visitor);
    default void bind(){}
    default Expr expand(){
        var unbinder = new Unbinder();
        var ident = unbinder.identify(this);
        var block = unbinder.toBlock(ident);
        return block;
    }
    default Expr expand(Unbinder unbinder){return null;}
    default Expr unbind(Unbinder unbinder){return null;}
    default Expr reduce(){
        var reducer = new Reducer();
        visit(reducer);
        return reducer.getResult();
    };
    default <T> T deserialize(Class<T> clazz, Context context){
        throw new NotImplementedException("Not implemented!");
    }
    default IdentExpr serialize(StringBuilder sb, Context context){
        throw new NotImplementedException("Not implemented!");
    }
}
