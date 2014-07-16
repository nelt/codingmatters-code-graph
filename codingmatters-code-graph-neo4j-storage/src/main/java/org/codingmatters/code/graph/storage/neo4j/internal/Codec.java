package org.codingmatters.code.graph.storage.neo4j.internal;

import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.api.references.Ref;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import scala.collection.Iterator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/06/14
 * Time: 01:33
 * To change this template use File | Settings | File Templates.
 */
public interface Codec {
    enum Label implements org.neo4j.graphdb.Label {
        CLASS, FIELD, METHOD       
    }
    
    enum RelationshipType implements org.neo4j.graphdb.RelationshipType {
        HAS_FIELD, HAS_METHOD, HAS_INNER_CLASS, USES, IMPLEMENTS, EXTENDS
    }
    
    final class Dictionnary {
        static public Label label(Ref ref) {
            if(ref instanceof ClassRef) {
                return Label.CLASS;
            } else if(ref instanceof FieldRef) {
                return Label.FIELD;
            } else if(ref instanceof MethodRef) {
                return Label.METHOD;
            } else {
                return null;
            }
        }
    }
    
    class Querier {
        
        private final ExecutionEngine engine;
        private final Neo4jPropertiesStorageProcessor processor = new Neo4jPropertiesStorageProcessor();
    
        public Querier(ExecutionEngine engine) {
            this.engine = engine;
        }
        
        public ResourceIterator<Object> mergeRefNode(Ref ref) {
            return this.mergeRefNode(ref, null);
        }
        
        public ResourceIterator<Object> mergeRefNode(Ref ref, Object data) {
            Neo4jPropertiesStorageProcessor.ToStore toStore;
            if(data != null) {
                toStore = this.processor.prepareStorage("n", data);
            } else {
                toStore = Neo4jPropertiesStorageProcessor.NOTHING;
            }
            
            String queryString = String.format(
                    "MERGE (n:%s {name: {name}}) " +
                            "ON CREATE SET n.source = {source}, n.shortName = {shortName}, n.created = timestamp(), n.updated = timestamp()%s " +
                            "ON MATCH  SET n.source = {source}, n.shortName = {shortName}, n.updated = timestamp()%s " +
                            "RETURN n", 
                    Dictionnary.label(ref).name(),
                    toStore.getPropertyMergerString(true),
                    toStore.getPropertyMergerString(true)
            );

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("source", ref.getSource());
            parameters.put("shortName", ref.getShortName());
            parameters.put("name", ref.getName());
            parameters.putAll(toStore.getParameters());

            return this.engine.execute(queryString, parameters).columnAs("n");
        }
    
        public ResourceIterator<Object> mergeRelationship(Ref source, RelationshipType relationshipType, Ref target) {
            Map<String, Object> parameters = new HashMap<>();
            
            String queryString = String.format(
                    "MATCH (source:%s {name: {sourceName}}), (target:%s {name: {targetName}}) " +
                            "MERGE (source)-[r:%s]->(target) " +
                            "RETURN r",
                    Dictionnary.label(source),
                    Dictionnary.label(target),
                    relationshipType.name()
            );
            parameters.put("sourceName", source.getName());
            parameters.put("targetName", target.getName());
            
            return this.engine.execute(queryString, parameters).columnAs("r");
        }
    }
}
