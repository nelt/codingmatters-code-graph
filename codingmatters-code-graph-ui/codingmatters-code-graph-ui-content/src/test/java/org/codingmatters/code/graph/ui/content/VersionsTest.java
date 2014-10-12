package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.ui.service.api.project.Project;
import org.codingmatters.code.graph.ui.service.api.project.Version;
import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.codingmatters.code.graph.test.support.text.TextFile.read;

public class VersionsTest {

    private Project project;

    @Before
    public void setUp() throws Exception {
        this.project = new Project("project 1/", "Project 1")
                .withDescription("Donec sed odio dui. Etiam porta sem malesuada magna mollis euismod. Nullam id dolor id nibh ultricies vehicula ut id elit.");
    }

    @Test
    public void testDigestLatest() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2014);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DAY_OF_MONTH, 4);
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 12);
        cal.set(Calendar.SECOND, 0);
        
        Version version = new Version(this.project, "18", true)
                .withDescription("Donec sed odio dui. Etiam porta sem malesuada magna mollis euismod. Nullam id dolor id nibh ultricies vehicula ut id elit.")
                .withDate(cal.getTime())
                ;

        Assertions.assertThat(Versions.digest(version).prefix("    ").content())
                .isEqualTo(read("project 1/index.html").htmlFragmenter("version-digest").next());
    }
}