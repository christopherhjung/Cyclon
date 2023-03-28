package org.cyclon;

import org.cyclon.mapper.Mapper;
import org.cyclon.parser.Parser;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class FileParseTest {
    public String load(String fileName){
        try{
            var filePath = ParserTest.class.getClassLoader().getResource(fileName).getPath();
            var bytes = Files.readAllBytes(Paths.get(filePath));
            return new String(bytes, StandardCharsets.UTF_8);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    public void mapCyclicMap(){
        var file = load("test.json");
        var dummy = Mapper.readValue(file, Map.class);
        System.out.println(dummy);
    }

    @Test
    public void cy(){
        var file = load("test.cyclon");
        var dummy = Mapper.readValue(file, Map.class);
        System.out.println(dummy);
    }
}
