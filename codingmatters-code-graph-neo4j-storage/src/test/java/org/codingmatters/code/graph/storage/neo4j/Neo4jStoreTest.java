package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.beans.ClassBean;
import org.codingmatters.code.graph.api.beans.MethodBean;
import org.codingmatters.code.graph.api.predicates.HasFieldPredicate;
import org.codingmatters.code.graph.api.predicates.HasMethodPredicate;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.util.StringLogger;
import scala.collection.immutable.Map;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 04/07/14
 * Time: 05:37
 * To change this template use File | Settings | File Templates.
 */
public class Neo4jStoreTest extends AbstractNeo4jProducerTest {
    
    private Neo4jNodeProducer nodeProducer;
    private Neo4jPredicateProducer predicateProducer;
    
    private Neo4jStore store;
    
    @Before
    public void setUp() throws Exception {
        this.nodeProducer = new Neo4jNodeProducer(this.getGraphDb(), this.getEngine());
        this.predicateProducer = new Neo4jPredicateProducer(this.getGraphDb(), this.getEngine());
            
        this.store = new Neo4jStore(this.getGraphDb(), this.getEngine());

        ClassRef clsRef = new ClassRef("TEST", "some/package/AClass");
        this.predicateProducer.hasField(new HasFieldPredicate(clsRef, new FieldRef("TEST", "some/package/AClass#field1")));
        this.predicateProducer.hasField(new HasFieldPredicate(clsRef, new FieldRef("TEST", "some/package/AClass#field2")));
        this.predicateProducer.hasMethod(new HasMethodPredicate(clsRef, new MethodRef("TEST", "some/package/AClass#method1():V")));
        this.predicateProducer.hasMethod(new HasMethodPredicate(clsRef, new MethodRef("TEST", "some/package/AClass#method2():V")));
    }

    @Test
    public void testGetClassBean() throws Exception {
        ClassBean actual = this.store.getClassBean(new ClassRef("TEST", "some/package/AClass"));
        
        Assert.assertEquals("some/package/AClass", actual.getRef());
        
        Assert.assertEquals(2, actual.getFields().size());
        Assert.assertEquals("some/package/AClass#field1", actual.getFields().get(0).getRef());
        
        Assert.assertEquals(2, actual.getMethods().size());
        Assert.assertEquals("some/package/AClass#method1():V", actual.getMethods().get(0).getRef());
    }
}
