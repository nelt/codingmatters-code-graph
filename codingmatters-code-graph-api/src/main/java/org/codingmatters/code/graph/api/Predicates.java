package org.codingmatters.code.graph.api;

import org.codingmatters.code.graph.api.predicates.*;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.MethodRef;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
public class Predicates {
    static public HasFieldPredicate hasField(ClassRef cls, FieldRef field) {
        return new HasFieldPredicate(cls, field);
    }
    static public HasMethodPredicate hasMethod(ClassRef cls, MethodRef method) {
        return new HasMethodPredicate(cls, method);
    }
    static public HasInnerClassPredicate hasInner(ClassRef cls, ClassRef inner) {
        return new HasInnerClassPredicate(cls, inner);
    }
    static public ExtendsPredicate extendsClass(ClassRef cls, ClassRef superClass) {
        return new ExtendsPredicate(cls, superClass);
    }
    static public UsesPredicate uses(MethodRef user, MethodRef used) {
        return new UsesPredicate(user, used);
    }
    static public UsesPredicate uses(MethodRef user, FieldRef used) {
        return new UsesPredicate(user, used);
    }
}
