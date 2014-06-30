package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.predicates.HasFieldPredicate;
import org.codingmatters.code.graph.api.predicates.HasInnerClassPredicate;
import org.codingmatters.code.graph.api.predicates.HasMethodPredicate;
import org.codingmatters.code.graph.api.predicates.UsesPredicate;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithAnonymousClass;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 30/06/14
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
public class AnonymousClassTest extends AbstractClassParserTest {

    public static final ClassRef CLASS_REF = new ClassRef(className(ClassWithAnonymousClass.class));
    public static final MethodRef CLASS_CONSTRUCTOR_REF = new MethodRef(CLASS_REF.getName() + "#<init>()V");
    public static final ClassRef ANONYMOUS_CLASS_REF = new ClassRef(CLASS_REF.getName() + "$1");
    public static final MethodRef ANONYMOUS_CLASS_CONSTRUCTOR_REF = new MethodRef(ANONYMOUS_CLASS_REF.getName() + "#<init>(Lorg/codingmatters/code/graph/bytecode/parser/parsed/ClassWithAnonymousClass;)V");

    @Test
    public void testClassWithAnonymousClass() throws Exception {
        this.getParser().parse(ClassWithAnonymousClass.class);
        
        this.assertProduced(
                new ClassNode(CLASS_REF),

                new ClassNode(ANONYMOUS_CLASS_REF),
                new FieldNode(new FieldRef(ANONYMOUS_CLASS_REF.getName() + "#this$0")),
                new HasFieldPredicate(
                        ANONYMOUS_CLASS_REF, new FieldRef(ANONYMOUS_CLASS_REF.getName() + "#this$0")
                ),
                new MethodNode(ANONYMOUS_CLASS_CONSTRUCTOR_REF),
                new HasMethodPredicate(ANONYMOUS_CLASS_REF, ANONYMOUS_CLASS_CONSTRUCTOR_REF),
                new UsesPredicate(ANONYMOUS_CLASS_CONSTRUCTOR_REF, new FieldRef(ANONYMOUS_CLASS_REF.getName() + "#this$0")),
                new UsesPredicate(ANONYMOUS_CLASS_CONSTRUCTOR_REF, OBJCT_CONSTRUCTOR_REF),
                new MethodNode(new MethodRef(ANONYMOUS_CLASS_REF.getName() +"#run()V")),
                new HasMethodPredicate(ANONYMOUS_CLASS_REF, new MethodRef(ANONYMOUS_CLASS_REF.getName() +"#run()V")),
                
                new HasInnerClassPredicate(CLASS_REF, ANONYMOUS_CLASS_REF),
                
                new MethodNode(CLASS_CONSTRUCTOR_REF),
                new HasMethodPredicate(CLASS_REF, CLASS_CONSTRUCTOR_REF),
                new UsesPredicate(CLASS_CONSTRUCTOR_REF, OBJCT_CONSTRUCTOR_REF),
                new MethodNode(new MethodRef(CLASS_REF.getName() + "#methodWithAnonymous()V")),
                new HasMethodPredicate(CLASS_REF, new MethodRef(CLASS_REF.getName() + "#methodWithAnonymous()V")),
                new UsesPredicate(new MethodRef(CLASS_REF.getName() + "#methodWithAnonymous()V"), ANONYMOUS_CLASS_CONSTRUCTOR_REF)
        );
    }
}
