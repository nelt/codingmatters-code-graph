package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.test.support.text.TextFile;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

import static org.codingmatters.code.graph.test.support.text.TextFile.read;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 06/10/14
 * Time: 07:02
 * To change this template use File | Settings | File Templates.
 */
public class NavigationsTest {
    
    @Test
    public void testIndex() throws Exception {
        assertThat(Navigations.index().content())
                .isEqualTo(read("index.html").htmlFragmenter("navigation bar").next());
    }

    @Test
    public void testProjectIndex() throws Exception {
        assertThat(Navigations.projectIndex().content())
                .isEqualTo(read("project 1/index.html").htmlFragmenter("navigation bar").next());
    }
}
