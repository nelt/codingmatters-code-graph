package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.predicates.HasFieldPredicate;
import org.codingmatters.code.graph.api.predicates.HasMethodPredicate;
import org.codingmatters.code.graph.api.predicates.UsesPredicate;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.junit.Assert;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/06/14
 * Time: 01:56
 * To change this template use File | Settings | File Templates.
 */
public class Neo4jPredicateProducerTest extends AbstractNeo4jProducerTest {
    
    private PredicateProducer producer;

    @Test
    public void testHasField() throws Exception {
        this.producer.hasField(new HasFieldPredicate(CLASS_REF, FIELD_REF));

        Assert.fail();
    }
    
    @Test
    public void testHasMethod() throws Exception {
        this.producer.hasMethod(new HasMethodPredicate(CLASS_REF, METHOD_REF));

        Assert.fail();
    }
    
    @Test
    public void testFieldUsage() throws Exception {
        this.producer.usage(new UsesPredicate(METHOD_REF, FIELD_REF));

        Assert.fail();
    }
    
    @Test
    public void testMethodUsage() throws Exception {
        this.producer.usage(new UsesPredicate(METHOD_REF, METHOD_REF));

        Assert.fail();
    }
}
