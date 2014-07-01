package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.Nodes;
import org.codingmatters.code.graph.api.Predicates;
import org.codingmatters.code.graph.api.predicates.ExtendsPredicate;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithInnerClass;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 28/06/14
 * Time: 21:59
 * To change this template use File | Settings | File Templates.
 */
public class InnerClassTest extends AbstractClassParserTest {

    public static final ClassRef CLASS_REF = new ClassRef(className(ClassWithInnerClass.class));
    public static final ClassRef INNER_CLASS_REF = new ClassRef(className(ClassWithInnerClass.class) + "$Inner");
    public static final MethodRef INNER_CLASS_CONSTRUCTOR_REF = new MethodRef(className(ClassWithInnerClass.class) + "$Inner#<init>()V");
    public static final MethodRef CLASS_CONSTRUCTOR_REF = new MethodRef(className(ClassWithInnerClass.class) + "#<init>()V");

    @Test
    public void testClassWithInnerClass() throws Exception {
        this.getParser().parse(ClassWithInnerClass.class);
        
        this.assertProduced(
                Nodes.classNode(CLASS_REF),
                defaultExtends(ClassWithInnerClass.class),
                Predicates.hasInner(CLASS_REF, INNER_CLASS_REF),
                Nodes.methodNode(CLASS_CONSTRUCTOR_REF),
                Predicates.hasMethod(CLASS_REF, CLASS_CONSTRUCTOR_REF),
                Predicates.uses(CLASS_CONSTRUCTOR_REF, OBJCT_CONSTRUCTOR_REF)
            );
    }

    @Test
    public void testInnerClass() throws Exception {
        this.getParser().parse(ClassWithInnerClass.class.getName() + "$Inner");

        this.assertProduced(
                Nodes.classNode(INNER_CLASS_REF),
                defaultExtends(ClassWithInnerClass.Inner.class),
                Nodes.methodNode(INNER_CLASS_CONSTRUCTOR_REF),
                Predicates.hasMethod(INNER_CLASS_REF, INNER_CLASS_CONSTRUCTOR_REF),
                Predicates.uses(INNER_CLASS_CONSTRUCTOR_REF, OBJCT_CONSTRUCTOR_REF)
        );
    }
}
