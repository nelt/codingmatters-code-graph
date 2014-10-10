package org.codingmatters.code.graph.ui.service.api.project;

/**
 * Created by nel on 10/10/14.
 */
public class Version {
    private final Project project;
    private final String name;

    public Version(Project project, String name) {
        this.project = project;
        this.name = name;
    }
    
    public Project project() {
        return this.project;
    }
    
    public String name() {
        return this.name;
    }
}
