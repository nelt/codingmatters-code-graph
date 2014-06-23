package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.predicates.HasFieldPredicate;
import org.codingmatters.code.graph.api.predicates.HasMethodPredicate;
import org.codingmatters.code.graph.api.predicates.UsesPredicate;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
public class AbstractClassParserTest {

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
        public void newHasMethod(HasMethodPredicate predicate) throws ProducerException {}
        @Override
        public void newHasField(HasFieldPredicate predicate) throws ProducerException {}
        @Override
        public void usage(UsesPredicate predicate) throws ProducerException {}
    };
    
    private ClassParser parser;
    private List<Object> produced = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        NodeProducer nodeProducer = this.getNodeProducer();
        PredicateProducer predicateProducer = this.getPredicateProducer();
        
        this.parser = new ClassParser(nodeProducer, predicateProducer);
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
                public void newHasMethod(HasMethodPredicate predicate) throws ProducerException {
                    produced.add(predicate);
                }
    
                @Override
                public void newHasField(HasFieldPredicate predicate) throws ProducerException {
                    produced.add(predicate);
                }

            @Override
            public void usage(UsesPredicate predicate) throws ProducerException {
                produced.add(predicate);
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        this.produced.clear();
    }

    protected void assertProduced(Object... objects) {
        objects = objects != null ? objects : new Object[0];
        Assert.assertEquals("unexpected produced count", objects.length, this.produced.size());
        for(int i = 0 ; i < objects.length ; i++) {
            Assert.assertEquals("unexpected " + (i+1) + "th produced", (objects[i]), this.produced.get(i));
        }
    }

    public ClassParser getParser() {
        return parser;
    }
}
