package org.cyclon.mapper;

import org.cyclon.parser.Parser;
import org.cyclon.visitor.Serializer;

public class Mapper {
    public static <T> T readValue(String cyclon, Class<T> clazz){
        var ast = Parser.parse(cyclon);
        return ast.deserialize(clazz);
    }

    public static String writeValue(Object obj){
        var serializer = new Serializer();
        var expr = serializer.serialize(obj);
        var expanded = expr.expand();
        return expanded.stringify();
    }
}
