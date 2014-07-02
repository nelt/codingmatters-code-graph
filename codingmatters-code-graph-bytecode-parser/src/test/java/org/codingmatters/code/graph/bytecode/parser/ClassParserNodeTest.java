package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.Nodes;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithField;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithMethod;
import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyClass;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class ClassParserNodeTest extends AbstractClassParserTest {

    @Override
    protected PredicateProducer getPredicateProducer() {
        return NOOP_PREDICATE_PRODUCER;
    }

    @Test
    public void testEmptyClass() throws Exception {
        this.getParser().parse(EmptyClass.class);
        this.assertProducedExactly(
                Nodes.classNode(new ClassRef(className(EmptyClass.class))),
                defaultConstructorNode(EmptyClass.class)
        );
    }


    @Test
    public void testClassWithField() throws Exception {
        this.getParser().parse(ClassWithField.class);
        this.assertProducedExactly(
                Nodes.classNode(new ClassRef(className(ClassWithField.class))),
                Nodes.fieldNode(new FieldRef(className(ClassWithField.class) + "#field")),
                defaultConstructorNode(ClassWithField.class)
        );
    }

    @Test
    public void testClassWithMethod() throws Exception {
        this.getParser().parse(ClassWithMethod.class);
        this.assertProducedExactly(
                Nodes.classNode(new ClassRef(className(ClassWithMethod.class))),
                defaultConstructorNode(ClassWithMethod.class),
                Nodes.methodNode(new MethodRef(className(ClassWithMethod.class) + "#method(Ljava/lang/Integer;Ljava/util/List;)Ljava/lang/String;"))
        );
    }
}
