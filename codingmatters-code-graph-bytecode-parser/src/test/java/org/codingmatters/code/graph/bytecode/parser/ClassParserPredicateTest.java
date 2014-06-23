package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.Predicates;
import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithField;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithMethodReadingField;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithMethodWritingField;
import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyClass;
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
    public void testClassWithMethodReadingField() throws Exception {
        this.getParser().parse(ClassWithMethodReadingField.class);
        this.assertProduced(
                Predicates.uses(
                        new MethodRef(ClassWithMethodReadingField.class.getName() + "#method(Lorg/codingmatters/code/graph/bytecode/parser/parsed/ClassWithField;):V"), 
                        new FieldRef(ClassWithField.class.getName() + "#field")
                )
        );
    }


    @Test
    public void testClassWithMethodWritingField() throws Exception {
        this.getParser().parse(ClassWithMethodWritingField.class);
        this.assertProduced(
                Predicates.uses(
                        new MethodRef(ClassWithMethodWritingField.class.getName() + "#method(Lorg/codingmatters/code/graph/bytecode/parser/parsed/ClassWithField;):V"),
                        new FieldRef(ClassWithField.class.getName() + "#field")
                )
        );
    }
}
