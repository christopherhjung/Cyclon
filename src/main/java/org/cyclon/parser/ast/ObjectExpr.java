package org.cyclon.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyclon.visitor.ResultVisitor;
import org.cyclon.visitor.Visitor;
import org.cyclon.mapper.Context;

import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@Getter
public class ObjectExpr implements Expr{
    private PairExpr[] pairs;

    @Override
    public <T> T deserialize(Class<T> clazz, Context context) {
        if(context.containsKey(this)){
            return (T)context.get(this);
        }

        Object obj;
        if(Map.class.isAssignableFrom(clazz) || Object.class.equals(clazz)){
            obj = deserializeMap(context);
        }else{
            obj = deserializePOJO(clazz, context);
        }

        context.remove(this);
        return (T)obj;
    }

    public Map<?,?> deserializeMap(Context context){
        var obj = new HashMap<>();
        context.put(this, obj);

        for( var pair : pairs){
            var key = pair.getKey().deserialize(Object.class, context);
            var value = pair.getValue().deserialize(Object.class, context);
            obj.put(key, value);
        }

        return obj;
    }

    public <T> T deserializePOJO(Class<T> clazz, Context context){
        try {
            var creator = clazz.getConstructor();
            var obj = creator.newInstance();

            context.put(this, obj);

            for( var pair : pairs){
                var key = (LiteralExpr)pair.getKey();
                var keyStr = (String)key.getValue();
                var field = clazz.getDeclaredField(keyStr);
                field.setAccessible(true);
                var fieldClass = field.getType();
                var value = pair.getValue().deserialize(fieldClass, context);
                field.set(obj, value);
            }

            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitObject(this);
    }

    @Override
    public <T> T visit(ResultVisitor<T> visitor) {
        return visitor.visitObject(this);
    }
}
