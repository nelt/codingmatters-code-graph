package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.api.references.Ref;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.util.StringLogger;
import scala.collection.Iterator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/06/14
 * Time: 00:20
 * To change this template use File | Settings | File Templates.
 */
public class Neo4jNodeProducer implements NodeProducer {
    private final GraphDatabaseService graphDb;
    private final ExecutionEngine engine;

    public Neo4jNodeProducer(GraphDatabaseService graphDb, ExecutionEngine engine) {
        this.graphDb = graphDb;
        this.engine = engine;
    }

    @Override
    public void aClass(ClassNode node) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            this.createOrUpdateRefNode(Codec.Label.CLASS, node.getRef());
            
            tx.success();
        }
    }

    @Override
    public void aField(FieldNode node) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            this.createOrUpdateRefNode(Codec.Label.FIELD, node.getRef());
            tx.success();
        }
    }

    @Override
    public void aMethod(MethodNode node) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            this.createOrUpdateRefNode(Codec.Label.METHOD, node.getRef());
            tx.success();
        }
    }
    
    private Node createOrUpdateRefNode(Label label, Ref ref) {
        String queryString = String.format("MERGE (n:%s {name: {name}}) RETURN n", label.name());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", ref.getName());
        Iterator<Node> resultIterator = engine.execute(queryString, parameters).columnAs("n");

        return resultIterator.next();
    }
}
