package org.cyclon.parser.ast;

import org.apache.commons.lang3.NotImplementedException;
import org.cyclon.mapper.Context;

public interface Expr {
    default void bind(){}
    Expr reduce();
    default <T> T deserialize(Class<T> clazz, Context context){
        throw new NotImplementedException("Not implemented!");
    }
    default IdentExpr serialize(StringBuilder sb, Context context){
        throw new NotImplementedException("Not implemented!");
    }
}
