package org.codingmatters.code.graph.api.references;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public class MethodRef implements UsableRef {

    private final String name;

    public MethodRef(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodRef methodRef = (MethodRef) o;

        if (name != null ? !name.equals(methodRef.name) : methodRef.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MethodRef{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }
}
