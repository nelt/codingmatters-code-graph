package org.codingmatters.code.graph.api.predicates;

import org.codingmatters.code.graph.api.references.ClassRef;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 28/06/14
 * Time: 22:13
 * To change this template use File | Settings | File Templates.
 */
public class HasInnerClassPredicate {
    private final ClassRef cls;
    private final ClassRef inner;

    public HasInnerClassPredicate(ClassRef cls, ClassRef inner) {
        this.cls = cls;
        this.inner = inner;
    }

    public ClassRef getCls() {
        return cls;
    }

    public ClassRef getInner() {
        return inner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HasInnerClassPredicate that = (HasInnerClassPredicate) o;

        if (cls != null ? !cls.equals(that.cls) : that.cls != null) return false;
        if (inner != null ? !inner.equals(that.inner) : that.inner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cls != null ? cls.hashCode() : 0;
        result = 31 * result + (inner != null ? inner.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HasInnerClassPredicate{" +
                "cls=" + cls +
                ", inner=" + inner +
                '}';
    }
}
