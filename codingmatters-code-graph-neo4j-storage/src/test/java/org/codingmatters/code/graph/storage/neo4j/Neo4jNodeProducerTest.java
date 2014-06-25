package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.kernel.impl.util.StringLogger;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/06/14
 * Time: 00:20
 * To change this template use File | Settings | File Templates.
 */
public class Neo4jNodeProducerTest extends AbstractNeo4jProducerTest {

    private NodeProducer producer;

    @Before
    public void setUpProducer() throws Exception {
        this.producer = new Neo4jNodeProducer(this.getGraphDb(), new ExecutionEngine(this.getGraphDb(), StringLogger.SYSTEM));
    }
    

    @Test
    public void testAClass() throws Exception {
        this.producer.aClass(new ClassNode(CLASS_REF));        
        this.producer.aClass(new ClassNode(CLASS_REF));
        
        assertUniqueNodeWithLabelAndName(Codec.Label.CLASS, CLASS_REF.getName());
    }

    @Test
    public void testAField() throws Exception {
        this.producer.aField(new FieldNode(FIELD_REF));
        this.producer.aField(new FieldNode(FIELD_REF));
        
        assertUniqueNodeWithLabelAndName(Codec.Label.FIELD, FIELD_REF.getName());
    }

    @Test
    public void testAMethod() throws Exception {
        this.producer.aMethod(new MethodNode(METHOD_REF));
        this.producer.aMethod(new MethodNode(METHOD_REF));
        
        assertUniqueNodeWithLabelAndName(Codec.Label.METHOD, METHOD_REF.getName());
    }
    
}
