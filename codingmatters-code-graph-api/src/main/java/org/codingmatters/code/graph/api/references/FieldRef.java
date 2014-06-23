package org.codingmatters.code.graph.api.references;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:09
 * To change this template use File | Settings | File Templates.
 */
public class FieldRef implements UsableRef {
    
    private final String name;
    
    public FieldRef(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldRef fieldRef = (FieldRef) o;

        if (name != null ? !name.equals(fieldRef.name) : fieldRef.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FieldRef{" +
                "name='" + name + '\'' +
                '}';
    }
}
