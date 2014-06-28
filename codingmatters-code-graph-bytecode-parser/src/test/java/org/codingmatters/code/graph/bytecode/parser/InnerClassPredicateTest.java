package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.Predicates;
import org.codingmatters.code.graph.api.predicates.HasInnerClassPredicate;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithField;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithInnerClass;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 28/06/14
 * Time: 22:16
 * To change this template use File | Settings | File Templates.
 */
public class InnerClassPredicateTest extends AbstractClassParserTest {    
    @Override
    protected NodeProducer getNodeProducer() {
        return NOOP_NODE_PRODUCER;
    }

    @Test
    public void testClassWithInnerClass() throws Exception {
        this.getParser().parse(ClassWithInnerClass.class);

        this.assertProduced(
                Predicates.hasInner(new ClassRef(className(ClassWithInnerClass.class)), new ClassRef(className(ClassWithInnerClass.class) + "$Inner")),
                hasDefaultConstructor(ClassWithInnerClass.class),
                usesDefaultConstructorPredicate(ClassWithInnerClass.class)
        );
    }
}
