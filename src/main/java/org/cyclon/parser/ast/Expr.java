package org.cyclon.parser.ast;

import org.apache.commons.lang3.NotImplementedException;
import org.cyclon.visitor.*;
import org.cyclon.mapper.Context;

public interface Expr {
    void visit(Visitor visitor);
    <T> T visit(ResultVisitor<T> visitor);
    default Expr expand(){
        var expander = new Expander();
        return expander.expand(this);
    }
    default Expr reduce(){
        var reducer = new Reducer();
        return reducer.reduce(this);
    };
    default <T> T deserialize(Class<T> clazz){
        var deserializer = new Deserializer();
        return deserializer.deserialize(this, clazz);
    }
    default String serialize(){
        var serializer = new Serializer();
        return serializer.serialize(this);
    }
}
