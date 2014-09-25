package org.codingmatters.code.graph.test.support.text;

import org.fest.assertions.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/09/14
 * Time: 06:57
 * To change this template use File | Settings | File Templates.
 */
public class TextFileReadTest {

    @Test
    public void testInputStream() throws Exception {
        TextFile actual = TextFile.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("tiny-file.txt"));

        assertThat(actual.content()).isEqualTo("hello");
    }
    
    @Test(expected = IOException.class)
    public void testNullInputStream() throws Exception {
        TextFile.read((InputStream) null);
    }
    
    @Test
    public void testResource() throws Exception {
        TextFile actual = TextFile.read("tiny-file.txt");
        
        assertThat(actual.content()).isEqualTo("hello");
    }
    
    @Test(expected = IOException.class)
    public void testUnexistentResource() throws Exception {
        TextFile.read("no-such-file.txt");
    }
    
    @Test
    public void testFile() throws Exception {
        TextFile actual = TextFile.read(new File(Thread.currentThread().getContextClassLoader().getResource("tiny-file.txt").toURI()));
        
        assertThat(actual.content()).isEqualTo("hello");
    }
    
    @Test(expected = IOException.class)
    public void testNoSuchFileFile() throws Exception {
        TextFile.read(new File("/no/such/file"));
    }

    @Test(expected = IOException.class)
    public void testNullFile() throws Exception {
        TextFile.read((File) null);
    }
    
    @Test
    public void testString() throws Exception {
        TextFile actual = TextFile.fromString("hello");

        assertThat(actual.content()).isEqualTo("hello");
    }

    @Test(expected = IOException.class)
    public void testNullString() throws Exception {
        TextFile.fromString(null);
    }
    
    
}