package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.predicates.*;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.neo4j.cypher.javacompat.ExecutionEngine;
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
        this.querier.mergeRefNode(predicate.getCls()).next();
        this.querier.mergeRefNode(predicate.getField()).next();
        this.querier.mergeRelationship(predicate.getCls(), Codec.RelationshipType.HAS_FIELD, predicate.getField());
    }

    @Override
    public void hasMethod(HasMethodPredicate predicate) throws ProducerException {
        this.querier.mergeRefNode(predicate.getCls());
        this.querier.mergeRefNode(predicate.getMethod());
        this.querier.mergeRelationship(predicate.getCls(), Codec.RelationshipType.HAS_METHOD, predicate.getMethod());
    }

    @Override
    public void hasInner(HasInnerClassPredicate predicate) throws ProducerException {
        this.querier.mergeRefNode(predicate.getCls());
        this.querier.mergeRefNode(predicate.getInner());
        this.querier.mergeRelationship(predicate.getCls(), Codec.RelationshipType.HAS_INNER_CLASS, predicate.getInner());
    }

    @Override
    public void hasParent(ExtendsPredicate predicate) throws ProducerException {
        this.querier.mergeRefNode(predicate.getCls());
        this.querier.mergeRefNode(predicate.getExtended());
        this.querier.mergeRelationship(predicate.getCls(), Codec.RelationshipType.EXTENDS, predicate.getExtended());
    }
    
    @Override
    public void usage(UsesPredicate predicate) throws ProducerException {
        this.querier.mergeRefNode(predicate.getUser());
        this.querier.mergeRefNode(predicate.getUsed());
        this.querier.mergeRelationship(predicate.getUser(), Codec.RelationshipType.USES, predicate.getUsed());
    }

    @Override
    public void hasInterface(ImplementsPredicate predicate) throws ProducerException {
        this.querier.mergeRefNode(predicate.getImplementer());
        this.querier.mergeRefNode(predicate.getImplemented());
        this.querier.mergeRelationship(predicate.getImplementer(), Codec.RelationshipType.IMPLEMENTS, predicate.getImplemented());
    }
}
