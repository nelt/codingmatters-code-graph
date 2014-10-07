package org.codingmatters.code.graph.ui.content.support;

import org.fest.assertions.api.Assertions;
import org.fest.assertions.api.StringAssert;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 07/10/14
 * Time: 06:23
 * To change this template use File | Settings | File Templates.
 */
public class LinesTest {
    
    @Test
    public void testPrefixTwoCompleteLines() throws Exception {
        assertThat(new Lines().appendLine("a").appendLine("b").prefix(" ").content())
                .isEqualTo(" a\n b\n");
    }

    @Test
    public void testPrefixTwoLines() throws Exception {

        assertThat(new Lines().appendLine("a").append("b").prefix(" ").content())
                .isEqualTo(" a\n b");
    }

    @Test
    public void testPrefixEmptyLines() throws Exception {
        assertThat(new Lines().append("").prefix(" ").content())
                .isEqualTo("");
    }

    @Test
    public void testEmptyLines() throws Exception {
        assertThat(new Lines().content())
                .isEqualTo("");
        assertThat(new Lines().append("").content())
                .isEqualTo("");
    }
}
