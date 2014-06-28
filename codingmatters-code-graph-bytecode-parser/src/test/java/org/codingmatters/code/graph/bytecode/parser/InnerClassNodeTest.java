package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.Nodes;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithInnerClass;
import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyClass;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 28/06/14
 * Time: 21:59
 * To change this template use File | Settings | File Templates.
 */
public class InnerClassNodeTest extends AbstractClassParserTest {


    @Override
    protected PredicateProducer getPredicateProducer() {
        return NOOP_PREDICATE_PRODUCER;
    }

    @Test
    public void testClassWithInnerClass() throws Exception {
        this.getParser().parse(ClassWithInnerClass.class);

        this.assertProduced(
                Nodes.classNode(new ClassRef(className(ClassWithInnerClass.class))),
                Nodes.classNode(new ClassRef(className(ClassWithInnerClass.class) + "$Inner")),
                defaultConstructorNode(ClassWithInnerClass.class)
        );
    }
}
