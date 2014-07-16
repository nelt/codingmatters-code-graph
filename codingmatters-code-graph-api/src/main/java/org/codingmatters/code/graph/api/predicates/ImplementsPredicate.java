package org.codingmatters.code.graph.api.predicates;

import org.codingmatters.code.graph.api.references.ClassRef;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 16/07/14
 * Time: 08:23
 * To change this template use File | Settings | File Templates.
 */
public class ImplementsPredicate {
    private final ClassRef implementer;
    private final ClassRef implemented;

    public ImplementsPredicate(ClassRef implementer, ClassRef implemented) {
        this.implementer = implementer;
        this.implemented = implemented;
    }

    public ClassRef getImplementer() {
        return implementer;
    }

    public ClassRef getImplemented() {
        return implemented;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImplementsPredicate that = (ImplementsPredicate) o;

        if (implemented != null ? !implemented.equals(that.implemented) : that.implemented != null) return false;
        if (implementer != null ? !implementer.equals(that.implementer) : that.implementer != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = implementer != null ? implementer.hashCode() : 0;
        result = 31 * result + (implemented != null ? implemented.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ImplementsPredicate{" +
                "implementer=" + implementer +
                ", implemented=" + implemented +
                '}';
    }
}
