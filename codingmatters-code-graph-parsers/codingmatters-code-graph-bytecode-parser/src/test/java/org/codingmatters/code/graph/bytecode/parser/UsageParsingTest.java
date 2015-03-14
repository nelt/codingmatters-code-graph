package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.predicates.*;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyClass;
import org.codingmatters.code.graph.bytecode.parser.parsed.usage.FieldUsage;
import org.codingmatters.code.graph.bytecode.parser.parsed.usage.MethodUsage;
import org.codingmatters.code.graph.bytecode.parser.parsed.usage.Used;
import org.codingmatters.code.graph.bytecode.parser.util.UsageAtLine;
import org.junit.Test;

import static org.codingmatters.code.graph.api.Predicates.uses;

/**
 * Created by nel on 13/03/15.
 */
public class UsageParsingTest extends AbstractClassParserTest {


    @Test
    public void testSuperConstructorUsage() throws Exception {
        this.getParser().parse(EmptyClass.class);

        assertUsageAt(method(EmptyClass.class, "<init>()V"), method(Object.class, "<init>()V"), 10);
    }

    @Test
    public void testFieldUsage() throws Exception {
        this.getParser().parse(FieldUsage.class);
        
        assertUsageAt(method(FieldUsage.class, "<init>()V"), field(FieldUsage.class, "field1"), 8);
        assertUsageAt(method(FieldUsage.class, "<init>()V"), field(FieldUsage.class, "field2"), 8);
        
        assertUsageAt(method(FieldUsage.class, "method()V"), field(FieldUsage.class, "field1"), 10);
        assertUsageAt(method(FieldUsage.class, "method()V"), field(FieldUsage.class, "field1"), 11);
        assertUsageAt(method(FieldUsage.class, "method()V"), field(FieldUsage.class, "field2"), 11);
    }

    @Test
    public void testMethodUsage() throws Exception {
        this.getParser().parse(MethodUsage.class);
        
        assertUsageAt(method(MethodUsage.class, "<init>()V"), method(Used.class, "value()Ljava/lang/String;"), 7);
        assertUsageAt(method(MethodUsage.class, "method()Ljava/lang/String;"), method(Used.class, "value()Ljava/lang/String;"), 9);
    }

    private void assertUsageAt(MethodRef user,  FieldRef used, int atLine) {
        assertProduced(usage(user, used, atLine));
    }
    private void assertUsageAt(MethodRef user,  MethodRef used, int atLine) {
        assertProduced(usage(user, used, atLine));
    }
    static private MethodRef method(Class clazz, String name) {
        return new MethodRef(className(clazz) + "#" + name);
    }
    
    static private FieldRef field(Class clazz, String name) {
        return new FieldRef(className(clazz) + "#" + name);
    }
    
    static private UsageAtLine usage(MethodRef user, FieldRef used, int atLine) {
        return new UsageAtLine(uses(user, used), atLine);
    }
    
    static private UsageAtLine usage(MethodRef user, MethodRef used, int atLine) {
        return new UsageAtLine(uses(user, used), atLine);
    }

    @Override
    protected NodeProducer getNodeProducer() {
        return NOOP_NODE_PRODUCER;
    }

    @Override
    protected PredicateProducer getPredicateProducer() {
        return new PredicateProducer() {
            @Override
            public void hasParent(ExtendsPredicate predicate) throws ProducerException {}
            @Override
            public void hasField(HasFieldPredicate predicate) throws ProducerException {}
            @Override
            public void hasMethod(HasMethodPredicate predicate) throws ProducerException {}
            @Override
            public void hasInner(HasInnerClassPredicate predicate) throws ProducerException {}
            @Override
            public void usage(UsesPredicate predicate, int atLine) throws ProducerException {
                produced(new UsageAtLine(predicate, atLine));
            }
            @Override
            public void hasInterface(ImplementsPredicate predicate) throws ProducerException {}
        };
    }
}
