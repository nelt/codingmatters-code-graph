package org.codingmatters.code.graph.test.support.text;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 06/10/14
 * Time: 06:47
 * To change this template use File | Settings | File Templates.
 */
public class HtmlFragmenterTest {

    @Test
    public void testFragment() throws Exception {
        assertThat(TextFile.fromString(
                "<!--TAG==-->\n" +
                        "fragment\n" +
                        "<!--==TAG-->"
        ).htmlFragmenter("TAG").next()).isEqualTo("fragment");
    }
    
    @Test
    public void testCleanFragment() throws Exception {
        assertThat(TextFile.fromString(
                "<!--TAG==-->\n" +
                        "frag\n" +
                        "<!--NOT A TAG==-->\n" +
                        "<!--==NOT A TAG-->\n" +
                        "ment\n" +
                        "<!--==TAG-->"
        ).htmlFragmenter("TAG").next()).isEqualTo("frag\nment");
    }
}
