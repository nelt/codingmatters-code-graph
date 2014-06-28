package org.codingmatters.code.graph.storage.neo4j.internal;

import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.api.references.Ref;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

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
        HAS_FIELD, HAS_METHOD, USES
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
}
