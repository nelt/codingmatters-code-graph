package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.beans.ClassBean;
import org.codingmatters.code.graph.api.beans.FieldBean;
import org.codingmatters.code.graph.api.beans.MethodBean;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.cross.cutting.logs.Log;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.ConstraintType;

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
    
    static private final Log log = Log.log(Neo4jStore.class);
    
    static public Runnable initializer(final GraphDatabaseService graphDb) {
        return new Runnable() {
            @Override
            public void run() {
                try(Transaction tx = graphDb.beginTx()) {

                    this.createUnicityConstraint(Codec.Label.CLASS, "name");
                    this.createUnicityConstraint(Codec.Label.FIELD, "name");
                    this.createUnicityConstraint(Codec.Label.METHOD, "name");
                    
                    tx.success();
                }

                try(Transaction tx = graphDb.beginTx()) {
                    graphDb.schema().awaitIndexesOnline(10, TimeUnit.SECONDS);
                }
            }

            private void createUnicityConstraint(Label label, String property) {
                if(! this.unicityConstraintExists(label, property)) {
                    graphDb.schema().constraintFor(label)
                            .assertPropertyIsUnique(property)
                            .create();
                    log.info("created %s.%s unique index", label.name(), property);
                }
            }

            private boolean unicityConstraintExists(Label label, String property) {
                boolean exists = false;
                for (ConstraintDefinition constraintDefinition : graphDb.schema().getConstraints()) {
                    if(
                            constraintDefinition.getLabel().equals(label) &&
                            constraintDefinition.isConstraintType(ConstraintType.UNIQUENESS)) {
                        for (String prop : constraintDefinition.getPropertyKeys()) {
                            if(prop.equals(property)) {
                                exists = true;
                            }
                        }
                    }
                }
                return exists;
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
