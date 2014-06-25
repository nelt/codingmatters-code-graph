package org.codingmatters.code.graph.api.references;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:08
 * To change this template use File | Settings | File Templates.
 */
public class ClassRef implements Ref {
    private final String name;

    public ClassRef(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassRef classRef = (ClassRef) o;

        if (name != null ? !name.equals(classRef.name) : classRef.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClassRef{" +
                "name='" + name + '\'' +
                '}';
    }
}
