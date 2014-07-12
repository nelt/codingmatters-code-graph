package org.codingmatters.code.graph.storage.neo4j;

import org.codingmatters.code.graph.api.beans.ClassBean;
import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.predicates.HasFieldPredicate;
import org.codingmatters.code.graph.api.predicates.HasMethodPredicate;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 04/07/14
 * Time: 05:37
 * To change this template use File | Settings | File Templates.
 */
public class Neo4jStoreTest extends AbstractNeo4jProducerTest {
    
    private Neo4jNodeProducer nodeProducer;
    private Neo4jPredicateProducer predicateProducer;
    
    private Neo4jStore store;
    
    @Before
    public void setUp() throws Exception {
        this.nodeProducer = new Neo4jNodeProducer(this.getGraphDb(), this.getEngine());
        this.predicateProducer = new Neo4jPredicateProducer(this.getGraphDb(), this.getEngine());
            
        this.store = new Neo4jStore(this.getGraphDb(), this.getEngine());

        ClassRef clsRef = new ClassRef("TEST", "some/package/AClass");
        FieldRef f1Ref = new FieldRef("TEST", "some/package/AClass#field1");
        FieldRef f2Ref = new FieldRef("TEST", "some/package/AClass#field2");
        MethodRef m1Ref = new MethodRef("TEST", "some/package/AClass#method1():V");
        MethodRef m2Ref = new MethodRef("TEST", "some/package/AClass#method2():V");

        this.nodeProducer.aClass(new ClassNode(clsRef));
        this.nodeProducer.aField(new FieldNode(f1Ref));
        this.nodeProducer.aField(new FieldNode(f2Ref));
        this.nodeProducer.aMethod(new MethodNode(m1Ref));
        this.nodeProducer.aMethod(new MethodNode(m2Ref));
        
        this.predicateProducer.hasField(new HasFieldPredicate(clsRef, f1Ref));
        this.predicateProducer.hasField(new HasFieldPredicate(clsRef, f2Ref));
        this.predicateProducer.hasMethod(new HasMethodPredicate(clsRef, m1Ref));
        this.predicateProducer.hasMethod(new HasMethodPredicate(clsRef, m2Ref));
    }

    @Test
    public void testGetClassBean() throws Exception {
        ClassBean actual = this.store.getClassBean(new ClassRef("TEST", "some/package/AClass"));

        assertThat(actual.getRef()).isEqualTo("some/package/AClass");
        assertThat(actual.getFields()).hasSize(2);
        assertThat(extractProperty("ref").from(actual.getFields()))
                .containsExactly("some/package/AClass#field1", "some/package/AClass#field2");

        assertThat(actual.getMethods()).hasSize(2);
        assertThat(extractProperty("ref").from(actual.getMethods()))
                .containsExactly("some/package/AClass#method1():V", "some/package/AClass#method2():V");
    }
}
