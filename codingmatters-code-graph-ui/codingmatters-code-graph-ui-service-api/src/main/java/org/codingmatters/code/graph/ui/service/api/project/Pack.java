package org.codingmatters.code.graph.ui.service.api.project;

/**
 * Created by nel on 10/10/14.
 */
public class Pack {
    private final Version version;
    private final String name;

    public Pack(Version version, String name) {
        this.version = version;
        this.name = name;
    }

    public Version version() {
        return version;
    }

    public String name() {
        return name;
    }
}
