package org.codingmatters.code.graph.api.predicates;

import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.api.references.UsableRef;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */
public class UsesPredicate {
    private final MethodRef user;
    private final UsableRef used;

    public UsesPredicate(MethodRef user, UsableRef used) {
        this.user = user;
        this.used = used;
    }

    public UsableRef getUsed() {
        return used;
    }

    public MethodRef getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsesPredicate that = (UsesPredicate) o;

        if (used != null ? !used.equals(that.used) : that.used != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (used != null ? used.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UsesPredicate{" +
                "used=" + used +
                ", user=" + user +
                '}';
    }
}
