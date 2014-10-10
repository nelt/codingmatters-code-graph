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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pack pack = (Pack) o;

        if (name != null ? !name.equals(pack.name) : pack.name != null) return false;
        if (version != null ? !version.equals(pack.version) : pack.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = version != null ? version.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
