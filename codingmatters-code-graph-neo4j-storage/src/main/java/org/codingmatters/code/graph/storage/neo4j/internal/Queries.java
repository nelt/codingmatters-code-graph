package org.codingmatters.code.graph.storage.neo4j.internal;

import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.Ref;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import scala.collection.Iterator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 27/06/14
 * Time: 06:44
 * To change this template use File | Settings | File Templates.
 */
public class Queries {
    static public Iterator<Node> mergeRefNodes(ExecutionEngine engine, Ref ref) {
        Map<String, Object> parameters = new HashMap<>();
        String queryString = String.format("MERGE (n:%s {name: {name}}) RETURN n", Codec.Dictionnary.label(ref).name());
        parameters.put("name", ref.getName());
        return engine.execute(queryString, parameters).columnAs("n");
    }

    public static Iterator<Relationship> mergeRelationship(
            ExecutionEngine engine, 
            Ref source, RelationshipType relationshipType, Ref target) {
        Map<String, Object> parameters = new HashMap<>();
        
        String queryString = String.format(
                "MATCH (source:%s {name: {sourceName}}), (target:%s {name: {targetName}}) " +
                        "MERGE (source)-[r:%s]->(target) " +
                        "RETURN r",
                Codec.Dictionnary.label(source),
                Codec.Dictionnary.label(target),
                relationshipType.name()
        );
        parameters.put("sourceName", source.getName());
        parameters.put("targetName", target.getName());
        
        return engine.execute(queryString, parameters).columnAs("r");
    }
}
