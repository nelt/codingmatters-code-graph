package org.codingmatters.code.graph.ui.service.api.project;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 07/10/14
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
public class Project {
    private final String path;
    private final String label;
    private String description;
    
    public Project(String path, String label) {
        this.path = path;
        this.label = label;
    }

    public String path() {
        return this.path;
    }

    public String label() {
        return this.label;
    }
    
    public String description() {
        return this.description;
    }

    public Project withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (description != null ? !description.equals(project.description) : project.description != null) return false;
        if (label != null ? !label.equals(project.label) : project.label != null) return false;
        if (path != null ? !path.equals(project.path) : project.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
