package org.cyon.core.mapper;

import org.cyon.core.parser.Parser;
import org.cyon.core.visitor.Serializer;

public class Mapper {
    public static <T> T readValue(String str, Class<T> clazz){
        var expr = Parser.parse(str);
        return expr.deserialize(clazz);
    }

    public static String writeValue(Object obj){
        var serializer = new Serializer();
        var expr = serializer.serialize(obj);
        var expanded = expr.expand();
        return expanded.stringify(true);
    }
}
