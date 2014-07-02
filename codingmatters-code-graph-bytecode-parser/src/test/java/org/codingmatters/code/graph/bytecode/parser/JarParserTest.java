package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.predicates.*;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithField;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithMethod;
import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyClass;
import org.codingmatters.code.graph.bytecode.parser.util.ClassResourcesHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 02/07/14
 * Time: 04:43
 * To change this template use File | Settings | File Templates.
 */
public class JarParserTest {
    private File jarFile;
    private NodeProducer nodeProducer = new NodeProducer() {
        @Override
        public void aClass(ClassNode node) throws ProducerException {produced.add(node);}
        @Override
        public void aField(FieldNode node) throws ProducerException {}
        @Override
        public void aMethod(MethodNode node) throws ProducerException {}
    };
    private PredicateProducer predicateProducer = new PredicateProducer() {
        @Override
        public void hasParent(ExtendsPredicate predicate) throws ProducerException {}
        @Override
        public void hasField(HasFieldPredicate predicate) throws ProducerException {}
        @Override
        public void hasMethod(HasMethodPredicate predicate) throws ProducerException {}
        @Override
        public void hasInner(HasInnerClassPredicate predicate) throws ProducerException {}
        @Override
        public void usage(UsesPredicate predicate) throws ProducerException {}
    };
    private LinkedList<Object> produced = new LinkedList<>();

    @Before
    public void setUp() throws Exception {
        this.jarFile = ClassResourcesHelper.makeTemporaryJarFile(EmptyClass.class, ClassWithField.class, ClassWithMethod.class);
        this.produced.clear();
    }

    @Test
    public void testThreeClassesParsed() throws Exception {
        JarParser.parse(this.jarFile, this.nodeProducer, this.predicateProducer, null);
        
        this.produced.contains(new ClassNode(new ClassRef(EmptyClass.class.getName().replace('.', '/'))));
        this.produced.contains(new ClassNode(new ClassRef(ClassWithField.class.getName().replace('.', '/'))));
        this.produced.contains(new ClassNode(new ClassRef(ClassWithMethod.class.getName().replace('.', '/'))));
    }
}
