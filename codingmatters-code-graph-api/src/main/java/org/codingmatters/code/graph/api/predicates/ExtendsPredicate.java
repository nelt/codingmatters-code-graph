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
    private final ClassRef extended;

    public ExtendsPredicate(ClassRef cls, ClassRef extended) {
        this.cls = cls;
        this.extended = extended;
    }

    public ClassRef getCls() {
        return cls;
    }

    public ClassRef getExtended() {
        return extended;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExtendsPredicate that = (ExtendsPredicate) o;

        if (cls != null ? !cls.equals(that.cls) : that.cls != null) return false;
        if (extended != null ? !extended.equals(that.extended) : that.extended != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cls != null ? cls.hashCode() : 0;
        result = 31 * result + (extended != null ? extended.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExtendsPredicate{" +
                "cls=" + cls +
                ", extended=" + extended +
                '}';
    }
}
