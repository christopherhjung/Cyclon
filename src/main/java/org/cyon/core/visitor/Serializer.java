package org.cyon.core.visitor;

import org.apache.commons.lang3.ClassUtils;
import org.cyon.core.Key;
import org.cyon.core.exception.SerializeException;
import org.cyon.core.parser.ast.*;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Serializer{
    private final Map<Object, Expr> map = new HashMap<>();
    public static final boolean REDUCE = true;

    private Object getKey(Object obj){
        if(REDUCE){
            return obj;
        }else{
            return new Key(obj);
        }
    }

    public Expr serialize(Object obj){
        if(obj == null) return LiteralExpr.NULL;

        var key = getKey(obj);
        var expr = map.get(key);
        if(expr != null)  return expr;

        var clazz = obj.getClass();
        if(clazz.isArray()){
            return serializeArray(key, obj);
        }else if(ClassUtils.isPrimitiveOrWrapper(clazz)){
            return serializeValue(key, obj);
        }else if(String.class.equals(clazz)){
            return serializeValue(key, obj);
        }else if(obj instanceof Collection<?>){
            return serializeCollection(key, obj);
        }else if(obj instanceof Map<?,?>){
            return serializeMap(key, obj);
        }

        return serializeObject(key, obj);
    }

    private LiteralExpr serializeValue(Object key, Object obj){
        var expr = new LiteralExpr(obj);
        map.put(key, expr);
        return expr;
    }

    private ListExpr serializeCollection(Object key, Object obj){
        var collection = (Collection<?>) obj;
        var expr = new ListExpr(null);
        map.put(key, expr);
        var elems = collection.stream()
                .map(this::serialize)
                .toArray(Expr[]::new);

        expr.setElems(elems);
        return expr;
    }

    private ObjectExpr serializeMap(Object key, Object obj){
        var map = (Map<?,?>) obj;
        var expr = new ObjectExpr(null);
        this.map.put(key, expr);
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

    private ListExpr serializeArray(Object key, Object obj){
        var length = Array.getLength(obj);
        var elems = new Expr[length];
        var expr = new ListExpr(elems);
        map.put(key, expr);

        for(var idx = 0; idx < length ; idx++){
            var elem = Array.get(obj, idx);
            elems[idx] = serialize(elem);
        }

        return expr;
    }

    private ObjectExpr serializeObject(Object key, Object obj){
        var clazz = obj.getClass();
        var pairs = new ArrayList<PairExpr>();
        var expr = new ObjectExpr(null);
        map.put(key, expr);
        try{
            for(var field : clazz.getDeclaredFields()){
                var modifier = field.getModifiers();
                if((modifier & Modifier.STATIC) == Modifier.STATIC){
                    continue;
                }else if((modifier & Modifier.PRIVATE) == Modifier.PRIVATE){
                    field.setAccessible(true);
                }

                var keyExpr = serialize(field.getName());
                var valueExpr = serialize(field.get(obj));
                pairs.add(new PairExpr(keyExpr, valueExpr));
            }

            expr.setPairs(pairs.toArray(PairExpr.EMPTY_ARRAY));
            return expr;
        }catch (IllegalAccessException e){
            throw new SerializeException("Error");
        }
    }
}
