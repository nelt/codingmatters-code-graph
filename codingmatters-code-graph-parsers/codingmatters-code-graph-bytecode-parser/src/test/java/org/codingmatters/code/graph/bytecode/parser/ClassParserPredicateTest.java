package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.Predicates;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.*;
import org.junit.Test;

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
    public void testClassWithField() throws Exception {
        this.getParser().parse(ClassWithField.class);
        this.assertProducedExactly(
                defaultExtends(ClassWithField.class),
                Predicates.hasField(
                        new ClassRef(className(ClassWithField.class)),
                        new FieldRef(className(ClassWithField.class) + "#field")
                ),
                hasDefaultConstructor(ClassWithField.class),
                usesDefaultConstructorPredicate(ClassWithField.class, 10)
        );
    }

    @Test
    public void testClassWithMethod() throws Exception {
        this.getParser().parse(ClassWithMethod.class);
        this.assertProducedExactly(
                defaultExtends(ClassWithMethod.class),
                hasDefaultConstructor(ClassWithMethod.class),
                usesDefaultConstructorPredicate(ClassWithMethod.class, 12),
                Predicates.hasMethod(
                        new ClassRef(className(ClassWithMethod.class)),
                        new MethodRef(className(ClassWithMethod.class) + "#method(Ljava/lang/Integer;Ljava/util/List;)Ljava/lang/String;")
                ),
                Predicates.hasMethod(
                        new ClassRef(className(ClassWithMethod.class)),
                        new MethodRef(className(ClassWithMethod.class) + "#varArgMethod([Ljava/lang/String;)V")
                ),
                Predicates.hasMethod(
                        new ClassRef(className(ClassWithMethod.class)),
                        new MethodRef(className(ClassWithMethod.class) + "#arrayMethod([Ljava/lang/String;)V")
                ),
                Predicates.hasMethod(
                        new ClassRef(className(ClassWithMethod.class)),
                        new MethodRef(className(ClassWithMethod.class) + "#primitiveMethod(IJFDC[I)V")
                )
        );
    }


    @Test
    public void testClassWithMethodReadingField() throws Exception {
        this.getParser().parse(ClassWithMethodReadingField.class);
        this.assertProducedExactly(
                defaultExtends(ClassWithMethodReadingField.class),
                hasDefaultConstructor(ClassWithMethodReadingField.class),
                usesDefaultConstructorPredicate(ClassWithMethodReadingField.class, 10),
                Predicates.hasMethod(
                        new ClassRef(className(ClassWithMethodReadingField.class)),
                        new MethodRef(className(ClassWithMethodReadingField.class) + "#method(Lorg/codingmatters/code/graph/bytecode/parser/parsed/ClassWithField;)V")
                ),
                Predicates.uses(
                        new MethodRef(className(ClassWithMethodReadingField.class) + "#method(Lorg/codingmatters/code/graph/bytecode/parser/parsed/ClassWithField;)V"),
                        new FieldRef(className(ClassWithField.class) + "#field"),
                        13
                )
        );
    }
    
    @Test
    public void testClassWithMethodWritingField() throws Exception {
        this.getParser().parse(ClassWithMethodWritingField.class);
        this.assertProducedExactly(
                defaultExtends(ClassWithMethodWritingField.class),
                hasDefaultConstructor(ClassWithMethodWritingField.class),
                usesDefaultConstructorPredicate(ClassWithMethodWritingField.class, 10),
                Predicates.hasMethod(
                        new ClassRef(className(ClassWithMethodWritingField.class)),
                        new MethodRef(className(ClassWithMethodWritingField.class) + "#method(Lorg/codingmatters/code/graph/bytecode/parser/parsed/ClassWithField;)V")
                ),
                Predicates.uses(
                        new MethodRef(className(ClassWithMethodWritingField.class) + "#method(Lorg/codingmatters/code/graph/bytecode/parser/parsed/ClassWithField;)V"),
                        new FieldRef(className(ClassWithField.class) + "#field"),
                        12
                )
        );
    }

    @Test
    public void testClassWithMethodInvokingMethod() throws Exception {
        this.getParser().parse(ClassWithMethodInvokingMethod.class);
        for (Object o : this.getProduced()) {
            System.out.println(o);
        }

        this.assertProducedExactly(
                defaultExtends(ClassWithMethodInvokingMethod.class),
                hasDefaultConstructor(ClassWithMethodInvokingMethod.class),
                usesDefaultConstructorPredicate(ClassWithMethodInvokingMethod.class, 10),
                Predicates.hasMethod(
                        new ClassRef(className(ClassWithMethodInvokingMethod.class)),
                        new MethodRef(className(ClassWithMethodInvokingMethod.class) + "#method(Lorg/codingmatters/code/graph/bytecode/parser/parsed/ClassWithMethod;)V")
                ),
                Predicates.uses(
                        new MethodRef(className(ClassWithMethodInvokingMethod.class) + "#method(Lorg/codingmatters/code/graph/bytecode/parser/parsed/ClassWithMethod;)V"),
                        new MethodRef(className(ClassWithMethod.class) + "#method(Ljava/lang/Integer;Ljava/util/List;)Ljava/lang/String;"),
                        13
                )
        );

    }

}
