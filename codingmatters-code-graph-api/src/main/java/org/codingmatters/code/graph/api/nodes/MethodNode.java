package org.codingmatters.code.graph.api.nodes;

import org.codingmatters.code.graph.api.references.MethodRef;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:23
 * To change this template use File | Settings | File Templates.
 */
public class MethodNode {
    private final MethodRef ref;

    public MethodNode(MethodRef ref) {
        this.ref = ref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodNode that = (MethodNode) o;

        if (ref != null ? !ref.equals(that.ref) : that.ref != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ref != null ? ref.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MethodNode{" +
                "ref=" + ref +
                '}';
    }
}
