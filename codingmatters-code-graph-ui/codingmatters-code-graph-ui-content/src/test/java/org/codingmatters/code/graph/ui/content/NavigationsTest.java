package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.ui.content.mocked.TestProjectService;
import org.codingmatters.code.graph.ui.service.api.project.Project;
import org.codingmatters.code.graph.ui.service.api.project.ProjectService;
import org.junit.Before;
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


    private ProjectService projectService;

    @Before
    public void setUp() throws Exception {
        this.projectService = new TestProjectService().withProjects(
                new Project("project 1/", "Project 1"),
                new Project("project 2/", "Project 2"),
                new Project("project 3/", "Project 3")
        );
    }

    @Test
    public void testIndex() throws Exception {
        assertThat(Navigations.index().content())
                .isEqualTo(read("index.html").htmlFragmenter("navigation bar").next());
    }

    @Test
    public void testProjectIndex() throws Exception {
        assertThat(Navigations.projectIndex(new Project("project 1/", "Project 1"), this.projectService).content())
                .isEqualTo(read("project 1/index.html").htmlFragmenter("navigation bar").next());
    }
}
