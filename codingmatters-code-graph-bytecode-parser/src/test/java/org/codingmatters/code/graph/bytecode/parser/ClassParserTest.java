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
public class ClassParserTest extends AbstractClassParserTest {

    @Test
    public void testEmptyClass() throws Exception {
        this.getParser().parse(EmptyClass.class);
        this.assertProduced(
                Nodes.classNode(new ClassRef(EmptyClass.class.getName())),
                Nodes.methodNode(new MethodRef(EmptyClass.class.getName() + "#<init>():V"))
        );
    }

    @Test
    public void testClassWithField() throws Exception {
        this.getParser().parse(ClassWithField.class);
        this.assertProduced(
                Nodes.classNode(new ClassRef(ClassWithField.class.getName())),
                Nodes.fieldNode(new FieldRef(ClassWithField.class.getName() + "#field")),
                Nodes.methodNode(new MethodRef(ClassWithField.class.getName() + "#<init>():V"))
        );
    }

    @Test
    public void testtestClassWithMethod() throws Exception {
        this.getParser().parse(ClassWithMethod.class);
        this.assertProduced(
                Nodes.classNode(new ClassRef(ClassWithMethod.class.getName())),
                Nodes.methodNode(new MethodRef(ClassWithMethod.class.getName() + "#<init>():V")),
                Nodes.methodNode(new MethodRef(ClassWithMethod.class.getName() + "#method(Ljava/lang/Integer;, Ljava/util/List;):Ljava/lang/String;"))
        );
    }
}
