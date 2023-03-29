package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyclon.mapper.Context;
import org.cyclon.mapper.Unbinder;

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
    public Expr expand(Unbinder unbinder) {
        var idx = 0;
        for(var elem : elems){
            elems[idx++] = unbinder.identify(elem);
        }
        return this;
    }

    @Override
    public Expr unbind(Unbinder unbinder) {
        return unbinder.identify(this);
    }

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
}
