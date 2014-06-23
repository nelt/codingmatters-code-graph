package org.codingmatters.code.graph.api.predicates;

import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:07
 * To change this template use File | Settings | File Templates.
 */
public class HasFieldPredicate {
    private final ClassRef cls;
    private final FieldRef field;

    public HasFieldPredicate(ClassRef cls, FieldRef field) {
        this.cls = cls;
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HasFieldPredicate that = (HasFieldPredicate) o;

        if (cls != null ? !cls.equals(that.cls) : that.cls != null) return false;
        if (field != null ? !field.equals(that.field) : that.field != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cls != null ? cls.hashCode() : 0;
        result = 31 * result + (field != null ? field.hashCode() : 0);
        return result;
    }
}
