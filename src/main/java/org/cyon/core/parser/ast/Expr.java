package org.cyon.core.parser.ast;

import org.cyon.core.visitor.*;
import org.cyon.core.visitor.stringifier.CompactStringifier;
import org.cyon.core.visitor.stringifier.PrettyStringifier;
import org.cyon.core.visitor.stringifier.Stringifier;

public interface Expr {
    Expr[] EMPTY_ARRAY = new Expr[0];

    void visit(Visitor visitor);
    <T> T visit(ResultVisitor<T> visitor);

    default Expr expand(){
        var expander = new Expander();
        return expander.expand(this);
    }

    default Expr reduce(){
        var reducer = new Reducer();
        return reducer.reduce(this);
    }

    default <T> T deserialize(Class<T> clazz){
        var deserializer = new Deserializer();
        return deserializer.deserialize(this, clazz);
    }

    default String stringify(boolean pretty){
        var stringifier = new PrettyStringifier(pretty);
        return stringifier.stringify(this);
    }
}
