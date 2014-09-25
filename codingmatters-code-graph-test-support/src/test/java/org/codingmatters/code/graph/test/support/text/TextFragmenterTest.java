package org.codingmatters.code.graph.test.support.text;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/09/14
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
public class TextFragmenterTest {

    @Test
    public void testNoNextFragment() throws Exception {
        assertThat(TextFile.fromString("").fragmenter("START", "END").hasNext()).isFalse();
    }
    
    @Test
    public void testHasNextFragment() throws Exception {
        assertThat(TextFile.fromString(
                "START\n" +
                "fragment\n" +
                "END"
        ).fragmenter("START", "END").hasNext()).isTrue();
    }
    
    @Test
    public void testNextFragment() throws Exception {
        assertThat(TextFile.fromString(
                "START\n" +
                "fragment\n" +
                "END"
        ).fragmenter("START", "END").next()).isEqualTo("fragment");
    }
    
    @Test
    public void testNextTwoFragment() throws Exception {
        TextFragmenter fragmenter = TextFile.fromString(
                "START\n" +
                "fragment1\n" +
                "END\n" +
                "START\n" +
                "fragment2\n" +
                "END"
        ).fragmenter("START", "END");
        
        assertThat(fragmenter.next()).isEqualTo("fragment1");
        assertThat(fragmenter.hasNext()).isTrue();
        assertThat(fragmenter.next()).isEqualTo("fragment2");
        assertThat(fragmenter.hasNext()).isFalse();
    }

    @Test(expected = NoSuchFragmentException.class)
    public void testNoStart() throws Exception {
        TextFile.fromString("").fragmenter("START", "END").next();
    }

    @Test(expected = NoSuchFragmentException.class)
    public void testNoEnd() throws Exception {
        TextFile.fromString("START").fragmenter("START", "END").next();
    }

    @Test(expected = NoSuchFragmentException.class)
    public void testNoEndAfterStart() throws Exception {
        TextFile.fromString("END\nSTART").fragmenter("START", "END").next();
    }
    
}
