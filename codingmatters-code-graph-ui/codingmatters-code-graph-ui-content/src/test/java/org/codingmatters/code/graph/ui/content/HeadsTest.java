package org.codingmatters.code.graph.ui.content;

import org.junit.Test;

import static org.codingmatters.code.graph.test.support.text.TextFile.read;
import static org.fest.assertions.api.Assertions.assertThat;

public class HeadsTest {

    @Test
    public void testIndex() throws Exception {
        assertThat(Heads.head("http://localhost/codingmatters-mockup/code-graph/").content())
                .isEqualTo(read("index.html").htmlFragmenter("head").next());
    }

    @Test
    public void testProject() throws Exception {
        assertThat(Heads.head("Project 1", "http://localhost/codingmatters-mockup/code-graph/").content())
                .isEqualTo(read("project 1/index.html").htmlFragmenter("head").next());
    }
    
}