package org.codingmatters.code.graph.api.nodes;

import org.codingmatters.code.graph.api.nodes.properties.ClassData;
import org.codingmatters.code.graph.api.references.ClassRef;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */
public class ClassNode {
    private final ClassRef ref;
    private final ClassData properties = new ClassData();

    public ClassNode(ClassRef ref) {
        this.ref = ref;
    }

    public ClassRef getRef() {
        return this.ref;
    }

    public ClassData getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassNode classNode = (ClassNode) o;

        if (ref != null ? !ref.equals(classNode.ref) : classNode.ref != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ref != null ? ref.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClassNode{" +
                "ref=" + ref +
                '}';
    }

}
