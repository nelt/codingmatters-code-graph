package org.codingmatters.code.graph.bytecode.parser.util;

import org.codingmatters.code.graph.api.predicates.UsesPredicate;

/**
 * Created by nel on 13/03/15.
 */
public class UsageAtLine {
    private final UsesPredicate predicate;
    private final int atLine;

    public UsageAtLine(UsesPredicate predicate, int atLine) {
        this.predicate = predicate;
        this.atLine = atLine;
    }

    public int getAtLine() {
        return atLine;
    }

    public UsesPredicate getPredicate() {
        return predicate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsageAtLine that = (UsageAtLine) o;

        if (atLine != that.atLine) return false;
        return !(predicate != null ? !predicate.equals(that.predicate) : that.predicate != null);

    }

    @Override
    public int hashCode() {
        int result = predicate != null ? predicate.hashCode() : 0;
        result = 31 * result + atLine;
        return result;
    }

    @Override
    public String toString() {
        return "UsageAtLine{" +
                "atLine=" + atLine +
                ", predicate=" + predicate +
                '}';
    }
}
