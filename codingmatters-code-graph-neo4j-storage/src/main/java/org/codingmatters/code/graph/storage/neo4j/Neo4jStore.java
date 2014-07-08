package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.beans.ClassBean;
import org.codingmatters.code.graph.api.beans.FieldBean;
import org.codingmatters.code.graph.api.beans.MethodBean;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.codingmatters.code.graph.storage.neo4j.internal.Codec.RelationshipType.HAS_FIELD;
import static org.codingmatters.code.graph.storage.neo4j.internal.Codec.RelationshipType.HAS_METHOD;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 04/07/14
 * Time: 05:36
 * To change this template use File | Settings | File Templates.
 */
public class Neo4jStore {
    
    static public Runnable initializer(final GraphDatabaseService graphDb) {
        return new Runnable() {
            @Override
            public void run() {
                try(Transaction tx = graphDb.beginTx()) {
                    graphDb.schema().constraintFor(Codec.Label.CLASS)
                            .assertPropertyIsUnique("name")
                            .create();
                    graphDb.schema()
                            .constraintFor(Codec.Label.FIELD)
                            .assertPropertyIsUnique("name")
                            .create();
                    graphDb.schema()
                            .constraintFor(Codec.Label.METHOD)
                            .assertPropertyIsUnique("name")
                            .create();
                    tx.success();
                }

                try(Transaction tx = graphDb.beginTx()) {
                    graphDb.schema().awaitIndexesOnline(10, TimeUnit.SECONDS);
                }
            }
        };
    }
    
    private final GraphDatabaseService graphDb;
    private final ExecutionEngine engine;

    public Neo4jStore(GraphDatabaseService graphDb, ExecutionEngine engine) {
        this.graphDb = graphDb;
        this.engine = engine;
    }

    public ClassBean getClassBean(ClassRef classRef) {
        try (Transaction tx = this.graphDb.beginTx();) {
            Map<String, Object> parameters = new HashMap<>();

            String queryString = String.format(
                    "MATCH " +
                            "(class:%s {name: {name}}) -[:" + HAS_FIELD + "|:" + HAS_METHOD + "]-> (member) " +
                            "RETURN member",
                    Codec.Dictionnary.label(classRef).name());
            parameters.put("name", classRef.getName());

            ExecutionResult membersResults = this.engine.execute(queryString, parameters);
            ClassBean result = new ClassBean();
            result.setRef(classRef.getShortName());
            
            for (Map<String, Object> membersResult : membersResults) {
                Node memberNode = (Node) membersResult.get("member");
                if(this.isField(memberNode)) {
                    FieldBean field = new FieldBean();
                    field.setRef((String) memberNode.getProperty("shortName"));
                    result.getFields().add(field);
                } else if(this.isMethod(memberNode)) {
                    MethodBean method = new MethodBean();
                    method.setRef((String) memberNode.getProperty("shortName"));
                    result.getMethods().add(method);
                }
            }

            return result;
        }
    }

    private boolean isField(Node node) {
        return this.hasLabel(node, Codec.Label.FIELD);
    }
    
    private boolean isMethod(Node node) {
        return this.hasLabel(node, Codec.Label.METHOD);
    }

    private boolean hasLabel(Node node, Codec.Label label) {
        for (Label nodeLabel : node.getLabels()) {
            if(label.name().equals(nodeLabel.name())) {
                return true;
            }
        }
        return false;
    }
}
