package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.Nodes;
import org.codingmatters.code.graph.api.Predicates;
import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.predicates.*;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.bytecode.parser.asm.ByteCodeResolver;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithMethod;
import org.codingmatters.code.graph.bytecode.parser.resolver.SystemResourcesResolver;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.api.filter.Filters;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.filter;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
public class AbstractClassParserTest {

    public static final MethodRef OBJCT_CONSTRUCTOR_REF = new MethodRef("java/lang/Object#<init>()V");

    public static String className(Class clazz) {
        return clazz.getName().replaceAll("\\.", "/");
    }
    
    public static final NodeProducer NOOP_NODE_PRODUCER = new NodeProducer() {
        @Override
        public void aClass(ClassNode node) throws ProducerException {}
        @Override
        public void aField(FieldNode node) throws ProducerException {}
        @Override
        public void aMethod(MethodNode node) throws ProducerException {}
    };
    
    public static final PredicateProducer NOOP_PREDICATE_PRODUCER = new PredicateProducer() {
        @Override
        public void hasMethod(HasMethodPredicate predicate) throws ProducerException {}
        @Override
        public void hasInner(HasInnerClassPredicate predicate) throws ProducerException {}
        @Override
        public void hasParent(ExtendsPredicate predicate) throws ProducerException {}
        @Override
        public void hasField(HasFieldPredicate predicate) throws ProducerException {}
        @Override
        public void usage(UsesPredicate predicate) throws ProducerException {}
        @Override
        public void hasInterface(ImplementsPredicate predicate) throws ProducerException {}
    };
    
    private ClassParser parser;
    private List<Object> produced = new ArrayList<>();

    static protected Filters<Object> filterClassNodes(List<Object> objects) {
        return filter(objects).with("class", ClassNode.class);
    }

    @Before
    public void setUp() throws Exception {
        NodeProducer nodeProducer = this.getNodeProducer();
        PredicateProducer predicateProducer = this.getPredicateProducer();
        
        this.parser = new ClassParser(nodeProducer, predicateProducer, createResolver(), this.parserSource());
    }

    public List<Object> getProduced() {
        return produced;
    }

    protected ByteCodeResolver createResolver() throws IOException {
        return new SystemResourcesResolver();
    }

    protected String parserSource() {
        return null;
    }

    protected NodeProducer getNodeProducer() {
        return new NodeProducer() {
                @Override
                public void aClass(ClassNode node) throws ProducerException {
                    produced.add(node);
                }
    
                @Override
                public void aField(FieldNode node) throws ProducerException {
                    produced.add(node);
                }
    
                @Override
                public void aMethod(MethodNode node) throws ProducerException {
                    produced.add(node);
                }
            };
    }

    protected PredicateProducer getPredicateProducer() {
        return new PredicateProducer() {
                @Override
                public void hasMethod(HasMethodPredicate predicate) throws ProducerException {
                    produced.add(predicate);
                }

            @Override
            public void hasInner(HasInnerClassPredicate predicate) throws ProducerException {
                produced.add(predicate);
            }

            @Override
            public void hasParent(ExtendsPredicate predicate) throws ProducerException {
                produced.add(predicate);
            }

            @Override
                public void hasField(HasFieldPredicate predicate) throws ProducerException {
                    produced.add(predicate);
                }

            @Override
            public void usage(UsesPredicate predicate) throws ProducerException {
                produced.add(predicate);
            }

            @Override
            public void hasInterface(ImplementsPredicate predicate) throws ProducerException {
                produced.add(predicate);
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        this.produced.clear();
    }
    
    protected void assertProduced(Object object) {
        Assertions.assertThat(this.produced).contains(object);
    }

    protected void assertProducedExactly(Object... objects) {
        objects = objects != null ? objects : new Object[0];
        Assertions.assertThat(this.produced).containsOnly(objects);
    }


    public ClassParser getParser() {
        return parser;
    }



    static protected ExtendsPredicate defaultExtends(Class clazz) {
        return new ExtendsPredicate(new ClassRef(className(clazz)), new ClassRef(className(Object.class)));
    }

    static protected MethodNode defaultConstructorNode(Class clazz) {
        return Nodes.methodNode(new MethodRef(className(clazz) + "#<init>()V"));
    }


    static protected UsesPredicate usesDefaultConstructorPredicate(Class clazz) {
        return Predicates.uses(
                new MethodRef(className(clazz) + "#<init>()V"),
                OBJCT_CONSTRUCTOR_REF
        );
    }

    static protected HasMethodPredicate hasDefaultConstructor(Class clazz) {
        return Predicates.hasMethod(
                new ClassRef(className(clazz)),
                new MethodRef(className(clazz) + "#<init>()V")
        );
    }
}
