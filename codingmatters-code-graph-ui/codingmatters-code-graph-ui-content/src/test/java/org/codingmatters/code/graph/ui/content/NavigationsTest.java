package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.ui.content.mocked.TestProjectService;
import org.codingmatters.code.graph.ui.service.api.project.Project;
import org.codingmatters.code.graph.ui.service.api.project.ProjectService;
import org.codingmatters.code.graph.ui.service.api.project.Version;
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
        Project project1 = new Project("project 1/", "Project 1");
        this.projectService = new TestProjectService().withProjects(
                project1,
                new Project("project 2/", "Project 2"),
                new Project("project 3/", "Project 3")
        ).withProjectVersion(
                project1,
                new Version(project1, "18", true),
                new Version(project1, "17"),
                new Version(project1, "16"),
                new Version(project1, "15"),
                new Version(project1, "14")
        );
    }

    @Test
    public void testIndex() throws Exception {
        assertThat(Navigations.index().content())
                .isEqualTo(read("index.html").htmlFragmenter("navigation bar").next());
    }

    @Test
    public void testProject() throws Exception {
        assertThat(Navigations.project(new Project("project 1/", "Project 1"), this.projectService).content())
                .isEqualTo(read("project 1/index.html").htmlFragmenter("navigation bar").next());
    }

    @Test
    public void testVersion() throws Exception {
        assertThat(Navigations.version(new Version(new Project("project 1/", "Project 1"), "18", true), this.projectService).content())
                .isEqualTo(read("project 1/18/index.html").htmlFragmenter("navigation bar").next());
    }
}
