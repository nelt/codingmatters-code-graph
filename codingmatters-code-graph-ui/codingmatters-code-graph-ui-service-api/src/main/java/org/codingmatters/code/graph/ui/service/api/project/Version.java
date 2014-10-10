package org.codingmatters.code.graph.ui.service.api.project;

/**
 * Created by nel on 10/10/14.
 */
public class Version {
    private final Project project;
    private final boolean latest;
    private final String name;

    public Version(Project project, String name) {
        this(project, name, false);
    }

    public Version(Project project, String name, boolean latest) {
        this.project = project;
        this.name = name;
        this.latest = latest;
    }

    public Project project() {
        return this.project;
    }
    
    public String name() {
        return this.name;
    }

    public boolean latest() {
        return latest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Version version = (Version) o;

        if (name != null ? !name.equals(version.name) : version.name != null) return false;
        if (project != null ? !project.equals(version.project) : version.project != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = project != null ? project.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
