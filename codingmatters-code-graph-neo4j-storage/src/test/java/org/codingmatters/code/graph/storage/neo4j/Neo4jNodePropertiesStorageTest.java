package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.nodes.properties.ClassInformationProperties;
import org.codingmatters.code.graph.api.nodes.properties.MethodSignatureProperties;
import org.codingmatters.code.graph.api.nodes.properties.SourceLocationProperties;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 13/07/14
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
public class Neo4jNodePropertiesStorageTest extends AbstractNeo4jProducerTest {

    public static final String METHOD_SIGNATURE = "method(Ljava/lang/Integer;Ljava/util/List;)Ljava/lang/String;";
    private NodeProducer producer;

    @Before
    public void setUpProducer() throws Exception {
        this.producer = new Neo4jNodeProducer(this.getGraphDb(), this.getEngine());
    }
    
    @Test
    public void testAClass() throws Exception {
        ClassNode node = new ClassNode(CLASS_REF);
        node.getProperties()
                .withInformation(new ClassInformationProperties.Builder()
                        .withClassName("path.to.Class")
                )
                .withDeclaration(new SourceLocationProperties.Builder()
                        .withLine(12)
                        .withStartColumn(5)
                        .withEndColumn(10)
                );
        this.producer.aClass(node);
        
        Node actual = assertUniqueNodeWithLabelAndName(Codec.Label.CLASS, CLASS_REF.getName());
        this.assertNodeHasRefProperties(CLASS_REF, actual);
        try( Transaction tx = this.getGraphDb().beginTx(); ) {
            assertThat(actual.getProperty("information_className")).isEqualTo("path.to.Class");
            assertThat(actual.getProperty("declaration_line")).isEqualTo(12);
            assertThat(actual.getProperty("declaration_startColumn")).isEqualTo(5);
            assertThat(actual.getProperty("declaration_endColumn")).isEqualTo(10);
        }
        
    }

    @Test
    public void testAMethod() throws Exception {
        MethodNode node = new MethodNode(METHOD_REF);
        node.getProperties().withSignature(MethodSignatureProperties.create().withSignature(METHOD_SIGNATURE));
        this.producer.aMethod(node);

        Node actual = assertUniqueNodeWithLabelAndName(Codec.Label.METHOD, METHOD_REF.getName());
        try( Transaction tx = this.getGraphDb().beginTx(); ) {
            assertThat(actual.getProperty("signature_signature")).isEqualTo(METHOD_SIGNATURE);
        }
    }
    
}
