package org.codingmatters.code.graph.storage.neo4j.postprocess;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 16/07/14
 * Time: 10:44
 * To change this template use File | Settings | File Templates.
 */
public class OverridesPostProcessor {

    private final GraphDatabaseService graphDb;
    private final ExecutionEngine engine;

    public OverridesPostProcessor(GraphDatabaseService graphDb, ExecutionEngine engine) {
        this.graphDb = graphDb;
        this.engine = engine;
    }
    
    public void process() {
        String query = "" +
                "MATCH (m2:METHOD)<-[:HAS_METHOD]-(c2:CLASS)-[:EXTENDS|IMPLEMENTS *]->(c1:CLASS)-[:HAS_METHOD]->(m1:METHOD) " +
                "WHERE m2.signature_signature = m1.signature_signature " +
                "MERGE (m2)-[o:OVERRIDES]->(m1) ";
        
        try(Transaction tx = this.graphDb.beginTx();) {
            ExecutionResult res = this.engine.execute(query);
            tx.success();
        }
        
    }
}
