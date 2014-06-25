package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.junit.Assert;
import org.junit.Before;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

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


    protected void assertUniqueNodeWithLabelAndName(Label label, String name) {
        try(
                Transaction tx = this.getGraphDb().beginTx();
                ResourceIterator<Node> nodes = this.getGraphDb().findNodesByLabelAndProperty(
                        label,
                        "name", name
                ).iterator();
        ) {
            nodes.next();
            Assert.assertFalse(nodes.hasNext());
        }
    }

}
