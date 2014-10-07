package org.codingmatters.code.graph.ui.content.mocked;

import org.codingmatters.code.graph.ui.service.api.project.Project;
import org.codingmatters.code.graph.ui.service.api.project.ProjectService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 07/10/14
 * Time: 22:17
 * To change this template use File | Settings | File Templates.
 */
public class TestProjectService implements ProjectService {
    private final List<Project> projects = new LinkedList<>();

    public TestProjectService withProjects(Project ... projects) {
        if(projects != null) {
            for (Project project : projects) {
                this.projects.add(project);
            }
        }
        return this;
    }
    
    @Override
    public List<Project> listProject() {
        return new ArrayList<>(this.projects);
    }
}
