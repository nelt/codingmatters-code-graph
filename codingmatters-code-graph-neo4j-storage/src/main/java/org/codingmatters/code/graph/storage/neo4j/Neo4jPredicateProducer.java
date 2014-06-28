package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.predicates.HasFieldPredicate;
import org.codingmatters.code.graph.api.predicates.HasMethodPredicate;
import org.codingmatters.code.graph.api.predicates.UsesPredicate;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.codingmatters.code.graph.storage.neo4j.internal.Queries;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
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
    private final ExecutionEngine engine;

    public Neo4jPredicateProducer(GraphDatabaseService graphDb, ExecutionEngine engine) {
        this.graphDb = graphDb;
        this.engine = engine;
    }

    @Override
    public void hasField(HasFieldPredicate predicate) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            Queries.mergeRefNodes(this.engine, predicate.getCls()).next();
            Queries.mergeRefNodes(this.engine, predicate.getField()).next();
            Queries.mergeRelationship(this.engine, predicate.getCls(), Codec.RelationshipType.HAS_FIELD, predicate.getField());
            tx.success();
        }
    }

    @Override
    public void hasMethod(HasMethodPredicate predicate) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            Queries.mergeRefNodes(this.engine, predicate.getCls());
            Queries.mergeRefNodes(this.engine, predicate.getMethod());
            Queries.mergeRelationship(this.engine, predicate.getCls(), Codec.RelationshipType.HAS_METHOD, predicate.getMethod());
            tx.success();
        }
    }

    @Override
    public void usage(UsesPredicate predicate) throws ProducerException {
        try(Transaction tx = this.graphDb.beginTx()) {
            Queries.mergeRefNodes(this.engine, predicate.getUser());
            Queries.mergeRefNodes(this.engine, predicate.getUsed());
            Queries.mergeRelationship(this.engine, predicate.getUser(), Codec.RelationshipType.USES, predicate.getUsed());
            tx.success();
        }
    }
}
