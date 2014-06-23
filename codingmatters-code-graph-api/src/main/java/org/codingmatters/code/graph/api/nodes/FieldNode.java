package org.codingmatters.code.graph.api.nodes;

import org.codingmatters.code.graph.api.references.FieldRef;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */
public class FieldNode {
    private final FieldRef ref;

    public FieldNode(FieldRef ref) {
        this.ref = ref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldNode fieldNode = (FieldNode) o;

        if (ref != null ? !ref.equals(fieldNode.ref) : fieldNode.ref != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ref != null ? ref.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FieldNode{" +
                "ref=" + ref +
                '}';
    }
}
