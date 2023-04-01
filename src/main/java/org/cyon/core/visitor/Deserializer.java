package org.cyon.core.visitor;

import org.apache.commons.lang3.NotImplementedException;
import org.cyon.core.mapper.Context;
import org.cyon.core.parser.ast.*;

import java.lang.reflect.Array;
import java.util.*;

public class Deserializer implements ResultVisitor<Object>{
    private final Context context = new Context();
    private Class<?> clazz;

    public Deserializer(){
    }

    public <T> T deserialize(Expr expr, Class<T> clazz){
        this.clazz = clazz;
        return (T)expr.visit(this);
    }

    @Override
    public Object visitLiteral(LiteralExpr literal) {
        return literal.getValue();
    }

    @Override
    public Object visitList(ListExpr list) {
        if(context.containsKey(list)){
            return context.get(list);
        }

        Object arr;
        if(clazz.isArray()){
            arr = deserializeArray(list);
        }else{
            arr = deserializeList(list);
        }

        //context.remove(list);
        return arr;
    }

    private Object deserializeArray(ListExpr list){
        var componentType = clazz.getComponentType();
        if(componentType == null){
            componentType = Object.class;
        }

        var elems = list.getElems();
        var arr = Array.newInstance(componentType, elems.length);
        context.put(list, arr);
        var idx = 0;
        for(var elem : elems){
            clazz = componentType;
            Array.set(arr, idx++, elem.visit(this));
        }

        return arr;
    }

    private Object deserializeList(ListExpr list){
        var componentType = clazz.getComponentType();
        if(componentType == null){
            componentType = Object.class;
        }

        var elems = list.getElems();
        var arr = new ArrayList<>();
        context.put(list, arr);
        for(var elem : elems){
            clazz = componentType;
            arr.add(elem.visit(this));
        }

        return arr;
    }

    @Override
    public Object visitObject(ObjectExpr expr) {
        if(context.containsKey(expr)){
            return context.get(expr);
        }

        Object obj;
        if(Map.class.isAssignableFrom(clazz) || Object.class.equals(clazz)){
            obj = deserializeMap(expr);
        }else{
            obj = deserializeObject(expr);
        }

        //context.remove(expr);
        return obj;
    }


    public Map<?,?> deserializeMap(ObjectExpr expr){
        var obj = new LinkedHashMap<>(4);
        context.put(expr, obj);

        var pairs = expr.getPairs();
        for( var pair : pairs){
            clazz = Object.class;
            var key = pair.getKey().visit(this);
            clazz = Object.class;
            var value = pair.getValue().visit(this);
            obj.put(key, value);
        }

        return obj;
    }

    public Object deserializeObject(ObjectExpr expr){
        try {
            var currentClazz = clazz;
            var creator = currentClazz.getConstructor();
            var obj = creator.newInstance();

            context.put(expr, obj);

            var pairs = expr.getPairs();
            for( var pair : pairs){
                var key = (LiteralExpr)pair.getKey();
                var keyStr = (String)key.getValue();
                var field = currentClazz.getDeclaredField(keyStr);
                field.setAccessible(true);
                clazz = field.getType();
                var value = pair.getValue().visit(this);
                field.set(obj, value);
            }

            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public Object visitPair(PairExpr block) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public Object visitIdent(IdentExpr ident) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public Object visitAssign(AssignExpr assign) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public Object visitBlock(BlockExpr block) {
        throw new NotImplementedException("Not supported");
    }
}
