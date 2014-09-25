package org.codingmatters.code.graph.test.support.text;

import org.fest.assertions.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

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
public class TextFileTest {

    @Test
    public void testReadInputStream() throws Exception {
        TextFile actual = TextFile.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("tiny-file.txt"));

        assertThat(actual.content()).isEqualTo("hello");
    }
    
    @Test(expected = IOException.class)
    public void testReadNullInputStream() throws Exception {
        TextFile actual = TextFile.read((InputStream) null);
    }
    
    
    @Test
    public void testReadResource() throws Exception {
        TextFile actual = TextFile.read("tiny-file.txt");
        
        assertThat(actual.content()).isEqualTo("hello");
    }
    @Test(expected = IOException.class)
    public void testReadUnexistentResource() throws Exception {
        TextFile actual = TextFile.read("no-such-file.txt");
    }
    
}
