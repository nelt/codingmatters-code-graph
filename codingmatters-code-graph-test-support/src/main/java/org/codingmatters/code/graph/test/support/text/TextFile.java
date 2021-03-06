package org.codingmatters.code.graph.test.support.text;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/09/14
 * Time: 06:56
 * To change this template use File | Settings | File Templates.
 */
public class TextFile {

    static public TextFile read(String resource) throws IOException {
        try (InputStream stream = resourceAsStream(resource)){
            return read(stream);
        }
    }

    static public TextFile read(File file) throws IOException {
        if(file == null) throw new IOException("null file");
        try(InputStream stream = new FileInputStream(file)) {
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

    static public TextFile fromString(String content) throws IOException {
        if(content == null) throw new IOException("null string");
        return new TextFile(content);
    }
    
    private static InputStream resourceAsStream(String resource) {
        if(Thread.currentThread().getContextClassLoader() != null) {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        } else {
            return null;
        }
    }
    
    private final String content;

    public TextFile(String content) {
        this.content = content;
    }

    public String content() {
        return this.content;
    }

    public TextFragmenter fragmenter(String startTag, String endTag) {
        return new TextFragmenter(this.content, startTag, endTag);
    }


    public TextFragmenter htmlFragmenter(String tag) {
        return new TextFragmenter(this.content, 
                "<!--" + tag + "==-->", 
                "<!--==" + tag + "-->")
                .cleaning("(<!--(.*)==-->(^|\\n))|(<!--==(.*)-->)(^|\\n)");
    }
}
