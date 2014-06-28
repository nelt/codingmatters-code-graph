package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.predicates.HasFieldPredicate;
import org.codingmatters.code.graph.api.predicates.HasMethodPredicate;
import org.codingmatters.code.graph.api.predicates.UsesPredicate;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.util.StringLogger;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/06/14
 * Time: 01:56
 * To change this template use File | Settings | File Templates.
 */
public class Neo4jPredicateProducerTest extends AbstractNeo4jProducerTest {
    
    private PredicateProducer producer;

    @Before
    public void setUpProducer() throws Exception {
        super.setUp();
        this.producer = new Neo4jPredicateProducer(this.getGraphDb(), new ExecutionEngine(this.getGraphDb(), StringLogger.SYSTEM));
    }

    @Test
    public void testHasField() throws Exception {
        this.producer.hasField(new HasFieldPredicate(CLASS_REF, FIELD_REF));
        this.producer.hasField(new HasFieldPredicate(CLASS_REF, FIELD_REF));

        Node clsNode = assertUniqueNodeWithLabelAndName(Codec.Label.CLASS, CLASS_REF.getName());
        Node fieldNode = assertUniqueNodeWithLabelAndName(Codec.Label.FIELD, FIELD_REF.getName());
        assertUniqueRelationship(clsNode, Codec.RelationshipType.HAS_FIELD, fieldNode);
    }
    
    @Test
    public void testHasMethod() throws Exception {
        this.producer.hasMethod(new HasMethodPredicate(CLASS_REF, METHOD_REF));
        this.producer.hasMethod(new HasMethodPredicate(CLASS_REF, METHOD_REF));

        Node clsNode = assertUniqueNodeWithLabelAndName(Codec.Label.CLASS, CLASS_REF.getName());
        Node methodNode = assertUniqueNodeWithLabelAndName(Codec.Label.METHOD, METHOD_REF.getName());
        assertUniqueRelationship(clsNode, Codec.RelationshipType.HAS_METHOD, methodNode);
    }
    
    @Test
    public void testFieldUsage() throws Exception {
        this.producer.usage(new UsesPredicate(METHOD_REF, FIELD_REF));
        this.producer.usage(new UsesPredicate(METHOD_REF, FIELD_REF));

        Node methodNode = assertUniqueNodeWithLabelAndName(Codec.Label.METHOD, METHOD_REF.getName());
        Node fieldNode = assertUniqueNodeWithLabelAndName(Codec.Label.FIELD, FIELD_REF.getName());
        assertUniqueRelationship(methodNode, Codec.RelationshipType.USES, fieldNode);
    }
    
    @Test
    public void testMethodUsage() throws Exception {
        this.producer.usage(new UsesPredicate(METHOD_REF, USED_METHOD_REF));
        this.producer.usage(new UsesPredicate(METHOD_REF, USED_METHOD_REF));

        Node methodNode = assertUniqueNodeWithLabelAndName(Codec.Label.METHOD, METHOD_REF.getName());
        Node usedMethodNode = assertUniqueNodeWithLabelAndName(Codec.Label.METHOD, USED_METHOD_REF.getName());
        assertUniqueRelationship(methodNode , Codec.RelationshipType.USES , usedMethodNode);
    }
}
