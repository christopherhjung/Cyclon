package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyclon.mapper.Context;

import java.lang.reflect.Array;


@AllArgsConstructor
@Getter
public class ListExpr implements Expr{
    private Expr[] elems;

    @Override
    public Expr reduce() {
        var idx = 0;
        for(var elem : elems){
            elems[idx++] = elem.reduce();
        }
        return this;
    }

    @Override
    public <T> T deserialize(Class<T> clazz, Context context) {
        if(context.containsKey(this)){
            return (T)context.get(this);
        }

        var arr = Array.newInstance(clazz.getComponentType(), elems.length);
        context.put(this, arr);
        var idx = 0;
        for(var elem : elems){
            Array.set(arr, idx++, elem.deserialize(Integer.class, context));
        }

        context.remove(this);
        return (T)arr;
    }
}
