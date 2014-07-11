package org.codingmatters.code.graph.bytecode.parser.functional;

import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.predicates.*;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.bytecode.parser.JarParser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.codingmatters.code.graph.bytecode.parser.functional.ContainsSourcePrefixOnlyOnce.containsSourcePrefixOnlyOnce;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 11/07/14
 * Time: 05:21
 * To change this template use File | Settings | File Templates.
 */
public class ParsingFunctionalTest {

    public static final String SOURCE = "TEST";
    private File jarFile;

    @Test
    public void testContainsSourcePrefixOnlyOnce() throws Exception {
        JarParser.parse(jarFile, this.sourcePrefixAsserterNodeProducer(), this.sourcePrefixAsserterPredicateProducer(), SOURCE);
    }

    @Test
    public void testCounters() throws Exception {
        final Counters counters = new Counters();
        JarParser.parse(jarFile, this.countingNodeProducer(counters), this.countingPredicateProducer(counters), SOURCE);
        
        assertThat(counters.classCounter, is(233));
        assertThat(counters.fieldCounter, is(318));
        assertThat(counters.methodCounter, is(1305));
        assertThat(counters.hasFieldCounter, is(318));
        assertThat(counters.hasMethodCounter, is(1305));
        assertThat(counters.hasInnerCounter, is(165));
        assertThat(counters.hasParentCounter, is(233));
        assertThat(counters.usageCounter, is(4521));
    }


    @Before
    public void setUp() throws Exception {
        this.jarFile = testJarFile();
    }


    private PredicateProducer sourcePrefixAsserterPredicateProducer() {
        return new PredicateProducer() {
            @Override
            public void hasParent(ExtendsPredicate predicate) throws ProducerException {
                assertThat(predicate.getCls(), containsSourcePrefixOnlyOnce(SOURCE));
                assertThat(predicate.getExtended(), containsSourcePrefixOnlyOnce(SOURCE));
            }
            @Override
            public void hasField(HasFieldPredicate predicate) throws ProducerException {
                assertThat(predicate.getCls(), containsSourcePrefixOnlyOnce(SOURCE));
                assertThat(predicate.getField(), containsSourcePrefixOnlyOnce(SOURCE));
            }
            @Override
            public void hasMethod(HasMethodPredicate predicate) throws ProducerException {
                assertThat(predicate.getCls(), containsSourcePrefixOnlyOnce(SOURCE));
                assertThat(predicate.getMethod(), containsSourcePrefixOnlyOnce(SOURCE));
            }
            @Override
            public void hasInner(HasInnerClassPredicate predicate) throws ProducerException {
                assertThat(predicate.getCls(), containsSourcePrefixOnlyOnce(SOURCE));
                assertThat(predicate.getInner(), containsSourcePrefixOnlyOnce(SOURCE));
            }
            @Override
            public void usage(UsesPredicate predicate) throws ProducerException {
                assertThat(predicate.getUser(), containsSourcePrefixOnlyOnce(SOURCE));
                assertThat(predicate.getUsed(), containsSourcePrefixOnlyOnce(SOURCE));
            }
        };
    }

    private NodeProducer sourcePrefixAsserterNodeProducer() {
        return new NodeProducer() {
            @Override
            public void aClass(ClassNode node) throws ProducerException {
                assertThat(node.getRef(), containsSourcePrefixOnlyOnce(SOURCE));
            }
            @Override
            public void aField(FieldNode node) throws ProducerException {
                assertThat(node.getRef(), containsSourcePrefixOnlyOnce(SOURCE));
            }
            @Override
            public void aMethod(MethodNode node) throws ProducerException {
                assertThat(node.getRef(), containsSourcePrefixOnlyOnce(SOURCE));
            }
        };
    }


    private PredicateProducer countingPredicateProducer(final Counters counters) {
        return new PredicateProducer() {
                @Override
                public void hasParent(ExtendsPredicate predicate) throws ProducerException {
                    counters.hasParentCounter++;
                }
    
                @Override
                public void hasField(HasFieldPredicate predicate) throws ProducerException {
                    counters.hasFieldCounter++;
                }
    
                @Override
                public void hasMethod(HasMethodPredicate predicate) throws ProducerException {
                    counters.hasMethodCounter++;
                }
    
                @Override
                public void hasInner(HasInnerClassPredicate predicate) throws ProducerException {
                    counters.hasInnerCounter++;
                }
    
                @Override
                public void usage(UsesPredicate predicate) throws ProducerException {
                    counters.usageCounter++;
                }
            };
    }

    private NodeProducer countingNodeProducer(final Counters counters) {
        return new NodeProducer() {
                @Override
                public void aClass(ClassNode node) throws ProducerException {
                    counters.classCounter++;
                }
                @Override
                public void aField(FieldNode node) throws ProducerException {
                    counters.fieldCounter++;
                }
                @Override
                public void aMethod(MethodNode node) throws ProducerException {
                    counters.methodCounter++;
                }
            };
    }

    static public class Counters {
        public int classCounter = 0;
        public int fieldCounter = 0;
        public int methodCounter = 0;
        public int hasParentCounter = 0;
        public int hasFieldCounter = 0;
        public int hasMethodCounter = 0;
        public int hasInnerCounter = 0;
        public int usageCounter = 0;
    }

    protected static File testJarFile() throws URISyntaxException {
        return new File(Thread.currentThread().getContextClassLoader().getResource("junit-4.11.jar").toURI());
    }

}
