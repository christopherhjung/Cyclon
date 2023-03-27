package org.cyclon.mapper;

import org.cyclon.parser.Parser;

public class Mapper {
    public static <T> T readValue(String cyclon, Class<T> clazz){
        var ast = Parser.parse(cyclon);
        return ast.deserialize(clazz, new Context());
    }
}
