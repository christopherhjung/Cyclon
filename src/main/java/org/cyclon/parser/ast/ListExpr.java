package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyclon.visitor.ResultVisitor;
import org.cyclon.visitor.Visitor;
import org.cyclon.mapper.Context;

import java.lang.reflect.Array;


@AllArgsConstructor
@Getter
public class ListExpr implements Expr{
    private Expr[] elems;

    @Override
    public <T> T deserialize(Class<T> clazz, Context context) {
        if(context.containsKey(this)){
            return (T)context.get(this);
        }

        var componentType = clazz.getComponentType();
        if(componentType == null){
            componentType = Object.class;
        }

        var arr = Array.newInstance(componentType, elems.length);
        context.put(this, arr);
        var idx = 0;
        for(var elem : elems){
            Array.set(arr, idx++, elem.deserialize(componentType, context));
        }

        context.remove(this);
        return (T)arr;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitList(this);
    }

    @Override
    public <T> T visit(ResultVisitor<T> visitor) {
        return visitor.visitList(this);
    }
}
