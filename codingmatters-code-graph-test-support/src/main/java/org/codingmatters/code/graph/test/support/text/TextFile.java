package org.codingmatters.code.graph.test.support.text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/09/14
 * Time: 06:56
 * To change this template use File | Settings | File Templates.
 */
public class TextFile {


    static public TextFile read(String resource) throws IOException {
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);){
            return read(stream);
        }
    }

    static public TextFile read(InputStream stream) throws IOException {
        if(stream == null) throw new IOException("null input stream");
        try (InputStreamReader reader = new InputStreamReader(stream);) {
            StringBuilder content = new StringBuilder();
            char [] buffer = new char[1024];
            
            for(int read = reader.read(buffer) ; read > -1 ; read = reader.read(buffer)) {
                content.append(buffer, 0, read);
            }
            
            return new TextFile(content.toString());
        }
    }

    private final String content;

    public TextFile(String content) {
        this.content = content;
    }

    public String content() {
        return this.content;
    }
}
