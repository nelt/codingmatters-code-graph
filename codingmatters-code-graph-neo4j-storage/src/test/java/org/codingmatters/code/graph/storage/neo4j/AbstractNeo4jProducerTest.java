package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.junit.Assert;
import org.junit.Before;
import org.neo4j.graphdb.*;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/06/14
 * Time: 01:55
 * To change this template use File | Settings | File Templates.
 */
public class AbstractNeo4jProducerTest extends AbstractNeo4jTest {

    public static final ClassRef CLASS_REF = new ClassRef("path/to/Class");
    public static final ClassRef ANOTHER_CLASS_REF = new ClassRef("path/to/AnotherClass");
    public static final ClassRef INNER_CLASS_REF = new ClassRef("path/to/Class$Inner");
    public static final FieldRef FIELD_REF = new FieldRef("path/to/Class#field");
    public static final MethodRef METHOD_REF = new MethodRef("path/to/Class#method(Ljava/lang/Integer;Ljava/util/List;)Ljava/lang/String;");
    public static final MethodRef USED_METHOD_REF = new MethodRef("path/to/Class#usedMethod(Ljava/lang/Integer;Ljava/util/List;)Ljava/lang/String;");


    @Before
    public void setUpIndexes() throws Exception {
        try(Transaction tx = this.getGraphDb().beginTx()) {
            this.getGraphDb().schema().constraintFor(Codec.Label.CLASS).assertPropertyIsUnique("name").create();

            this.getGraphDb().schema().constraintFor(Codec.Label.FIELD).assertPropertyIsUnique("name").create();

            this.getGraphDb().schema().constraintFor(Codec.Label.METHOD).assertPropertyIsUnique("name").create();
            
            tx.success();
        }
        try(Transaction tx = this.getGraphDb().beginTx()) {
            this.getGraphDb().schema().awaitIndexesOnline(10, TimeUnit.SECONDS);
        }
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
    
    protected Relationship assertUniqueRelationship(Node source, RelationshipType relationshipType, Node target) {
        try(
                Transaction tx = this.getGraphDb().beginTx()
        ) {
            Relationship found = null;
            for (Relationship relationship : source.getRelationships(Direction.OUTGOING, relationshipType)) {
                if(relationship.getEndNode().equals(target)) {
                    Assert.assertNull("duplicate relationship " + relationshipType, found);
                    found = relationship;
                }
            }
            Assert.assertNotNull("no such relationship " + source + " -" + relationshipType + "-> " + target, found);
            return found;
        }
    }

}
