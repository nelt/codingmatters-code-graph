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
}
