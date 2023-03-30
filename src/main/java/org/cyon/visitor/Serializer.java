package org.cyon.visitor;

import org.apache.commons.lang3.ClassUtils;
import org.cyon.parser.ast.*;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Serializer{
    private final Map<Object, Expr> map = new HashMap<>();

    public Expr serialize(Object obj){
        if(map.containsKey(obj)){
            return map.get(obj);
        }

        var clazz = obj.getClass();
        if(clazz.isArray()){
            return serializeArray(obj);
        }else if(ClassUtils.isPrimitiveOrWrapper(clazz)){
            return serializeValue(obj);
        }else if(String.class.equals(clazz)){
            return serializeValue(obj);
        }else if(obj instanceof Collection<?>){
            return serializeCollection(obj);
        }else if(obj instanceof Map<?,?>){
            return serializeMap(obj);
        }

        return serializeObject(obj);
    }

    private LiteralExpr serializeValue(Object obj){
        var expr = new LiteralExpr(obj);
        map.put(obj, expr);
        return expr;
    }

    private ListExpr serializeCollection(Object obj){
        var collection = (Collection<?>) obj;
        var expr = new ListExpr(null);
        map.put(obj, expr);
        var elems = collection.stream()
                .map(this::serialize)
                .toArray(Expr[]::new);

        expr.setElems(elems);
        return expr;
    }

    private ObjectExpr serializeMap(Object obj){
        var map = (Map<?,?>) obj;
        var expr = new ObjectExpr(null);
        this.map.put(obj, expr);
        var pairs = map.entrySet()
                .stream()
                .map(it -> new PairExpr(
                    serialize(it.getKey()),
                    serialize(it.getValue())
                ))
                .toArray(PairExpr[]::new);

        expr.setPairs(pairs);
        return expr;
    }

    private ListExpr serializeArray(Object obj){
        var length = Array.getLength(obj);
        var elems = new Expr[length];
        var expr = new ListExpr(elems);
        map.put(obj, expr);

        for(var idx = 0; idx < length ; idx++){
            var elem = Array.get(obj, idx);
            elems[idx] = serialize(elem);
        }

        return expr;
    }

    private ObjectExpr serializeObject(Object obj){
        var clazz = obj.getClass();
        var pairs = new ArrayList<PairExpr>();
        var expr = new ObjectExpr(null);
        map.put(obj, expr);
        try{
            for(var field : clazz.getDeclaredFields()){
                if((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC){
                    continue;
                }

                field.setAccessible(true);
                var value = field.get(obj);

                var keyExpr = new LiteralExpr(field.getName());
                var valueExpr = serialize(value);
                pairs.add(new PairExpr(keyExpr, valueExpr));
            }

            expr.setPairs(pairs.toArray(new PairExpr[0]));
            return expr;
        }catch (Exception e){
            throw new RuntimeException("Error");
        }
    }
}
