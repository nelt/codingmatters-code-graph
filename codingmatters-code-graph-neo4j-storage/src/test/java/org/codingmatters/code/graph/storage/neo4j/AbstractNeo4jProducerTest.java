package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.api.references.Ref;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.junit.Assert;
import org.junit.Before;
import org.neo4j.graphdb.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/06/14
 * Time: 01:55
 * To change this template use File | Settings | File Templates.
 */
public class AbstractNeo4jProducerTest extends AbstractNeo4jTest {

    public static final ClassRef CLASS_REF = new ClassRef("source", "path/to/Class");
    public static final ClassRef ANOTHER_CLASS_REF = new ClassRef("source", "path/to/AnotherClass");
    public static final ClassRef INNER_CLASS_REF = new ClassRef("source", "path/to/Class$Inner");
    public static final FieldRef FIELD_REF = new FieldRef("source", "path/to/Class#field");
    public static final MethodRef METHOD_REF = new MethodRef("source", "path/to/Class#method(Ljava/lang/Integer;Ljava/util/List;)Ljava/lang/String;");
    public static final MethodRef USED_METHOD_REF = new MethodRef("source", "path/to/Class#usedMethod(Ljava/lang/Integer;Ljava/util/List;)Ljava/lang/String;");

    @Before
    public void setUpIndexes() throws Exception {
        Neo4jStore.initializer(this.getGraphDb()).run();
    }

    protected Node assertUniqueNodeWithLabelAndName(Label label, String name) {
        try(
                Transaction tx = this.getGraphDb().beginTx();
                ResourceIterator<Node> nodes = this.getGraphDb().findNodesByLabelAndProperty(
                        label,
                        "name", name
                ).iterator();
        ) {
            Node result = nodes.next();
            Assert.assertFalse("duplicated node with label " + label + " and name " + name, nodes.hasNext());
            return result;
        }
    }

    protected void assertNoSuchRelationship(Node source, RelationshipType relationshipType, Node target) {
        try(
                Transaction tx = this.getGraphDb().beginTx()
        ) {
            Assert.assertFalse("relationship exists " + source + " -" + relationshipType + "-> " + target, source.getRelationships(Direction.OUTGOING, relationshipType).iterator().hasNext());
        }
    }
    
    protected List<Relationship> assertRelationshipExists(Node source, RelationshipType relationshipType, Node target) {
        try(
                Transaction tx = this.getGraphDb().beginTx()
        ) {
            List<Relationship> found = new LinkedList<>();
            for (Relationship relationship : source.getRelationships(Direction.OUTGOING, relationshipType)) {
                if(relationship.getEndNode().equals(target)) {
                    found.add(relationship);
                }
            }
            Assert.assertFalse("no such relationship " + source + " -" + relationshipType + "-> " + target, found.isEmpty());
            return found;
        }
    }

    protected List<Relationship> assertRelationshipCount(int expectedCount, Node source, RelationshipType relationshipType, Node target) {
        List<Relationship> found = this.assertRelationshipExists(source, relationshipType, target);
        Assert.assertEquals("wrong count for relationship " + source + " -" + relationshipType + "-> " + target, expectedCount, found.size());
        return found;
    }
    
    protected Relationship assertUniqueRelationship(Node source, RelationshipType relationshipType, Node target) {
        return this.assertRelationshipCount(1, source, relationshipType, target).get(0);
    }



    protected void assertNodeHasRefProperties(Ref ref, Node actual) {
        try( Transaction tx = this.getGraphDb().beginTx(); ) {
            Assert.assertEquals(ref.getSource(), actual.getProperty("source"));
            Assert.assertEquals(ref.getShortName(), actual.getProperty("shortName"));
            Assert.assertEquals(ref.getName(), actual.getProperty("name"));
        }
    }

}
