package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.predicates.*;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
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
public class Neo4jPredicateProducer implements PredicateProducer {
    private final GraphDatabaseService graphDb;
    private final Codec.Querier querier;

    public Neo4jPredicateProducer(GraphDatabaseService graphDb, ExecutionEngine engine) {
        this.graphDb = graphDb;
        this.querier = new Codec.Querier(engine);
    }

    @Override
    public void hasField(HasFieldPredicate predicate) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            this.querier.mergeRefNodes(predicate.getCls()).next();
            this.querier.mergeRefNodes(predicate.getField()).next();
            this.querier.mergeRelationship(predicate.getCls(), Codec.RelationshipType.HAS_FIELD, predicate.getField());
            tx.success();
        }
    }

    @Override
    public void hasMethod(HasMethodPredicate predicate) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            this.querier.mergeRefNodes(predicate.getCls());
            this.querier.mergeRefNodes(predicate.getMethod());
            this.querier.mergeRelationship(predicate.getCls(), Codec.RelationshipType.HAS_METHOD, predicate.getMethod());
            tx.success();
        }
    }

    @Override
    public void hasInner(HasInnerClassPredicate predicate) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            this.querier.mergeRefNodes(predicate.getCls());
            this.querier.mergeRefNodes(predicate.getInner());
            this.querier.mergeRelationship(predicate.getCls(), Codec.RelationshipType.HAS_INNER_CLASS, predicate.getInner());
            tx.success();
        }
    }

    @Override
    public void hasParent(ExtendsPredicate predicate) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            this.querier.mergeRefNodes(predicate.getCls());
            this.querier.mergeRefNodes(predicate.getExtended());
            this.querier.mergeRelationship(predicate.getCls(), Codec.RelationshipType.EXTENDS, predicate.getExtended());
            tx.success();
        }
    }
    
    @Override
    public void usage(UsesPredicate predicate) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            this.querier.mergeRefNodes(predicate.getUser());
            this.querier.mergeRefNodes(predicate.getUsed());
            this.querier.mergeRelationship(predicate.getUser(), Codec.RelationshipType.USES, predicate.getUsed());
            tx.success();
        }
    }
}
