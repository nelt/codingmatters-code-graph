package org.codingmatters.code.graph.ui.service.api.project;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by nel on 10/10/14.
 */
public class Version {
    private final Project project;
    private final boolean latest;
    private final String name;

    private String description = "";
    private Date date;
    
    public Version(Project project, String name) {
        this(project, name, false);
    }

    public Version(Project project, String name, boolean latest) {
        this.project = project;
        this.name = name;
        this.latest = latest;
    }

    public Version withDescription(String description) {
        this.description = description;
        return this;
    }

    public Version withDate(Date date) {
        this.date = date;
        return this;
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


    public String description() {
        return this.description;
    }

    public Date date() {
        return this.date;
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
