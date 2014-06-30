package org.codingmatters.code.graph.api.predicates;

import org.codingmatters.code.graph.api.references.ClassRef;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 30/06/14
 * Time: 22:24
 * To change this template use File | Settings | File Templates.
 */
public class ExtendsPredicate {
    private final ClassRef cls;
    private final ClassRef parent;

    public ExtendsPredicate(ClassRef cls, ClassRef parent) {
        this.cls = cls;
        this.parent = parent;
    }

    public ClassRef getCls() {
        return cls;
    }

    public ClassRef getParent() {
        return parent;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExtendsPredicate that = (ExtendsPredicate) o;

        if (cls != null ? !cls.equals(that.cls) : that.cls != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cls != null ? cls.hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExtendsPredicate{" +
                "cls=" + cls +
                ", parent=" + parent +
                '}';
    }
}
