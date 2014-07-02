package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/06/14
 * Time: 00:20
 * To change this template use File | Settings | File Templates.
 */
public class Neo4jNodeProducer implements NodeProducer {
    private final GraphDatabaseService graphDb;
    private final Codec.Querier querier;

    public Neo4jNodeProducer(GraphDatabaseService graphDb, ExecutionEngine engine) {
        this.graphDb = graphDb;
        this.querier = new Codec.Querier(engine);
    }

    @Override
    public void aClass(ClassNode node) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            this.querier.mergeRefNode(node.getRef());
            tx.success();
        }
    }

    @Override
    public void aField(FieldNode node) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            this.querier.mergeRefNode(node.getRef());
            tx.success();
        }
    }

    @Override
    public void aMethod(MethodNode node) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            this.querier.mergeRefNode(node.getRef());
            tx.success();
        }
    }

}
