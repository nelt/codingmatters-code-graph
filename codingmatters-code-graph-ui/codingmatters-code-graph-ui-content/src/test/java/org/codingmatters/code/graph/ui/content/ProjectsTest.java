package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.ui.content.mocked.TestProjectService;
import org.codingmatters.code.graph.ui.service.api.project.Project;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.codingmatters.code.graph.test.support.text.TextFile.read;
import static org.fest.assertions.api.Assertions.assertThat;

public class ProjectsTest {


    @Test
    public void testDigest() throws Exception {
        Project project = new Project("project 1/", "Project 1")
                .withDescription("Donec sed odio dui. Etiam porta sem malesuada magna mollis euismod. Nullam id dolor id nibh ultricies vehicula ut id elit.");

        assertThat(Projects.digest(project).prefix("                ").content())
                .isEqualTo(read("index.html").htmlFragmenter("project digest").next());
    }

    @Test
    public void testDigestList() throws Exception {
        List<Project> projects = new LinkedList<>();
        for(int i = 1 ; i <= 4 ; i++) {
            projects.add(
                    new Project("project " + i + "/", "Project " + i)
                        .withDescription("Donec sed odio dui. Etiam porta sem malesuada magna mollis euismod. Nullam id dolor id nibh ultricies vehicula ut id elit.")
            );
        }
        
        assertThat(Projects.digestList(new TestProjectService().withProjects(projects)).prefix("        ").content())
                .isEqualTo(read("index.html").htmlFragmenter("project digest list").next());
    }

    @Test
    public void testProjectAsHeader() throws Exception {
        Project project = new Project("project 1/", "Project 1")
                .withDescription("Donec sed odio dui. Etiam porta sem malesuada magna mollis euismod. Nullam id dolor id nibh ultricies vehicula ut id elit.");
        
        assertThat(Projects.header(project).prefix("    ").content())
                .isEqualTo(read("project 1/index.html").htmlFragmenter("header").next());
    }
}