package org.cyon.core;

import org.cyon.core.visitor.sequence.AlphaNumericIdentSequence;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IdentSequenceTest {
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
    public void cy(){
        var identSequence = new AlphaNumericIdentSequence();

        for(var i = 0 ; i < 10000 ; i++){
            System.out.printf("%d : %s\n", i, identSequence.next());
        }
    }
}
