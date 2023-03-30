package org.cyon.mapper;

import org.cyon.parser.Parser;
import org.cyon.visitor.Serializer;

public class Mapper {
    public static <T> T readValue(String cyclon, Class<T> clazz){
        var ast = Parser.parse(cyclon);
        return ast.deserialize(clazz);
    }

    public static String writeValue(Object obj){
        var serializer = new Serializer();
        var expr = serializer.serialize(obj);
        var expanded = expr.expand();
        return expanded.stringify(true);
    }
}
