package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.ui.service.api.project.Pack;
import org.codingmatters.code.graph.ui.service.api.project.Project;
import org.codingmatters.code.graph.ui.service.api.project.Version;
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
        assertThat(Heads.head(new Project(null, "Project 1"), "http://localhost/codingmatters-mockup/code-graph/").content())
                .isEqualTo(read("project 1/index.html").htmlFragmenter("head").next());
    }

    @Test
    public void testVersion() throws Exception {
        assertThat(Heads.head(new Version(new Project(null, "Project 1"), "18"), "http://localhost/codingmatters-mockup/code-graph/").content())
                .isEqualTo(read("project 1/18/index.html").htmlFragmenter("head").next());
    }

    @Test
    public void testPackage() throws Exception {
        assertThat(Heads.head(new Pack(new Version(new Project(null, "Project 1"), "18"), "org.codingmatters.code.graph.bytecode.parser.util"), "http://localhost/codingmatters-mockup/code-graph/").content())
                .isEqualTo(read("project 1/18/org/codingmatters/code/graph/bytecode/parser/util/index.html").htmlFragmenter("head").next());
    }
    
}