package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.Nodes;
import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.predicates.HasFieldPredicate;
import org.codingmatters.code.graph.api.predicates.HasMethodPredicate;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithField;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithMethod;
import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        Class clazz = EmptyClass.class;
        this.assertProduced(
                Nodes.classNode(new ClassRef(className(clazz))),
                Nodes.methodNode(new MethodRef(className(EmptyClass.class) + "#<init>()V"))
        );
    }

    @Test
    public void testClassWithField() throws Exception {
        this.getParser().parse(ClassWithField.class);
        this.assertProduced(
                Nodes.classNode(new ClassRef(className(ClassWithField.class))),
                Nodes.fieldNode(new FieldRef(className(ClassWithField.class) + "#field")),
                Nodes.methodNode(new MethodRef(className(ClassWithField.class) + "#<init>()V"))
        );
    }

    @Test
    public void testtestClassWithMethod() throws Exception {
        this.getParser().parse(ClassWithMethod.class);
        this.assertProduced(
                Nodes.classNode(new ClassRef(className(ClassWithMethod.class))),
                Nodes.methodNode(new MethodRef(className(ClassWithMethod.class) + "#<init>()V")),
                Nodes.methodNode(new MethodRef(className(ClassWithMethod.class) + "#method(Ljava/lang/Integer;Ljava/util/List;)Ljava/lang/String;"))
        );
    }
}
