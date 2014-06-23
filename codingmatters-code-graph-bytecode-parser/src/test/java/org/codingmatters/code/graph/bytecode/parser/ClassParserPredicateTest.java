package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.Predicates;
import org.codingmatters.code.graph.api.predicates.UsesPredicate;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.*;
import org.junit.Test;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 23/06/14
 * Time: 10:07
 * To change this template use File | Settings | File Templates.
 */
public class ClassParserPredicateTest extends AbstractClassParserTest {
    @Override
    protected NodeProducer getNodeProducer() {
        return NOOP_NODE_PRODUCER;
    }

    @Test
    public void testClassWithMethodReadingField() throws Exception {
        this.getParser().parse(ClassWithMethodReadingField.class);
        Class clazz = ClassWithMethodReadingField.class;
        this.assertProduced(
                defaultConstructorPredicate(ClassWithMethodReadingField.class),
                Predicates.uses(
                        new MethodRef(className(clazz) + "#method(Lorg/codingmatters/code/graph/bytecode/parser/parsed/ClassWithField;)V"), 
                        new FieldRef(className(ClassWithField.class) + "#field")
                )
        );
    }
    
    @Test
    public void testClassWithMethodWritingField() throws Exception {
        this.getParser().parse(ClassWithMethodWritingField.class);
        this.assertProduced(
                defaultConstructorPredicate(ClassWithMethodWritingField.class),
                Predicates.uses(
                        new MethodRef(className(ClassWithMethodWritingField.class) + "#method(Lorg/codingmatters/code/graph/bytecode/parser/parsed/ClassWithField;)V"),
                        new FieldRef(className(ClassWithField.class) + "#field")
                )
        );
    }

    @Test
    public void testClassWithMethodInvokingMethod() throws Exception {
        this.getParser().parse(ClassWithMethodInvokingMethod.class);
        this.assertProduced(
                defaultConstructorPredicate(ClassWithMethodInvokingMethod.class),
                Predicates.uses(
                        new MethodRef(className(ClassWithMethodInvokingMethod.class) + "#method(Lorg/codingmatters/code/graph/bytecode/parser/parsed/ClassWithMethod;)V"),
                        new MethodRef(className(ClassWithMethod.class) + "#method(Ljava/lang/Integer;Ljava/util/List;)Ljava/lang/String;")
                )
        );

    }

    static private UsesPredicate defaultConstructorPredicate(Class clazz) {
        return Predicates.uses(
                new MethodRef(className(clazz) + "#<init>()V"),
                new MethodRef("java/lang/Object#<init>()V")
        );
    }
}
