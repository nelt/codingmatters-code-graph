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
        this.assertProduced(
                Nodes.classNode(new ClassRef("source", className(EmptyClass.class))),
                Predicates.extendsClass(new ClassRef("source", className(EmptyClass.class)), new ClassRef("source", "java/lang/Object")),
                Nodes.methodNode(new MethodRef("source", className(EmptyClass.class) + "#<init>()V")),
                Predicates.hasMethod(new ClassRef("source", className(EmptyClass.class)), new MethodRef("source", className(EmptyClass.class) + "#<init>()V")),
                Predicates.uses(new MethodRef("source", className(EmptyClass.class) + "#<init>()V"), new MethodRef("source", "java/lang/Object#<init>()V"))
        );
    }
}
