package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.predicates.ExtendsPredicate;
import org.codingmatters.code.graph.api.predicates.HasMethodPredicate;
import org.codingmatters.code.graph.api.predicates.UsesPredicate;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyClass;
import org.codingmatters.code.graph.bytecode.parser.parsed.EmptySubclass;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 30/06/14
 * Time: 22:16
 * To change this template use File | Settings | File Templates.
 */
public class SubclassingTest extends AbstractClassParserTest {

    public static final ClassRef EMPTY_SUBCLASS_REF = new ClassRef(className(EmptySubclass.class));
    public static final MethodRef EMPTY_SUBCLASS_CONSTRUCTOR_REF = new MethodRef(EMPTY_SUBCLASS_REF.getName() + "#<init>()V");
    public static final ClassRef EMPTY_CLASS_REF = new ClassRef(className(EmptyClass.class));

    @Test
    public void testEmptySubclass() throws Exception {
        this.getParser().parse(EmptySubclass.class);
        
        this.assertProducedExactly(
                new ClassNode(EMPTY_SUBCLASS_REF),
                new ExtendsPredicate(EMPTY_SUBCLASS_REF, EMPTY_CLASS_REF),
                new MethodNode(EMPTY_SUBCLASS_CONSTRUCTOR_REF),
                new HasMethodPredicate(EMPTY_SUBCLASS_REF, EMPTY_SUBCLASS_CONSTRUCTOR_REF),
                new UsesPredicate(EMPTY_SUBCLASS_CONSTRUCTOR_REF, new MethodRef(className(EmptyClass.class) + "#<init>()V"))
        );
    }
}
