package org.codingmatters.code.graph.ui.content.mocked;

import org.codingmatters.code.graph.ui.service.api.project.Project;
import org.codingmatters.code.graph.ui.service.api.project.ProjectService;
import org.codingmatters.code.graph.ui.service.api.project.Version;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 07/10/14
 * Time: 22:17
 * To change this template use File | Settings | File Templates.
 */
public class TestProjectService implements ProjectService {
    private final List<Project> projects = new LinkedList<>();
    private final Map<Project, List<Version>> versions = new HashMap<>();
    
    public TestProjectService withProjects(List<Project> projects) {
        return withProjects(projects.toArray(new Project[projects.size()]));
    }
    
    public TestProjectService withProjects(Project ... projects) {
        if(projects != null) {
            for (Project project : projects) {
                this.projects.add(project);
            }
        }
        return this;
    }
    
    public TestProjectService withProjectVersion(Project project, Version ... versions) {
        if(! this.versions.containsKey(project)) {
            this.versions.put(project, new ArrayList<Version>());
        }
        for (Version version : versions) {
            this.versions.get(project).add(version);
        }
        return this;
    }
    
    @Override
    public List<Project> listProject() {
        return new ArrayList<>(this.projects);
    }

    @Override
    public List<Version> listProjectVersions(Project project) {
        if(! this.versions.containsKey(project)) return new ArrayList<>();
        return new ArrayList<>(this.versions.get(project));
    }
}
