package org.codingmatters.code.graph.api.predicates;

import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.MethodRef;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class HasMethodPredicate {
    private final ClassRef cls;
    private final MethodRef method;

    public HasMethodPredicate(ClassRef cls, MethodRef method) {
        this.cls = cls;
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HasMethodPredicate that = (HasMethodPredicate) o;

        if (cls != null ? !cls.equals(that.cls) : that.cls != null) return false;
        if (method != null ? !method.equals(that.method) : that.method != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cls != null ? cls.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HasMethodPredicate{" +
                "cls=" + cls +
                ", method=" + method +
                '}';
    }
}
