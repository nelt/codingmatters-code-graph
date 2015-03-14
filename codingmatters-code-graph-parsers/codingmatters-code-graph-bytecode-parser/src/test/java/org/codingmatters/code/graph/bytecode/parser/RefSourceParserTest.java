package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.Nodes;
import org.codingmatters.code.graph.api.Predicates;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyClass;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 02/07/14
 * Time: 01:56
 * To change this template use File | Settings | File Templates.
 */
public class RefSourceParserTest extends AbstractClassParserTest {
    @Override
    protected String parserSource() {
        return "source";
    }

    @Test
    public void testEmptyClass() throws Exception {
        this.getParser().parse(EmptyClass.class);
        this.assertProducedExactly(
                Nodes.classNode(clazz("source", EmptyClass.class)),
                Predicates.extendsClass(clazz("source", EmptyClass.class), clazz("source", Object.class)),
                Nodes.methodNode(method("source", EmptyClass.class, "<init>()V")),
                Predicates.hasMethod(clazz("source", EmptyClass.class), method("source", EmptyClass.class, "<init>()V")),
                Predicates.uses(method("source", EmptyClass.class, "<init>()V"), method("source", Object.class, "<init>()V"), 10)
        );
    }
}
