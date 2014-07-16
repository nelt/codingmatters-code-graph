package org.codingmatters.code.graph.storage.neo4j.postprocess;

import org.codingmatters.code.graph.api.Predicates;
import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.nodes.properties.MethodSignatureProperties;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.storage.neo4j.AbstractNeo4jProducerTest;
import org.codingmatters.code.graph.storage.neo4j.Neo4jNodeProducer;
import org.codingmatters.code.graph.storage.neo4j.Neo4jPredicateProducer;
import org.codingmatters.code.graph.storage.neo4j.internal.Codec;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.Node;

import static org.codingmatters.code.graph.api.Predicates.extendsClass;
import static org.codingmatters.code.graph.api.Predicates.implementsInterface;
import static org.codingmatters.code.graph.storage.neo4j.internal.Codec.RelationshipType.OVERRIDES;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 16/07/14
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */
public class OverridesPostProcessorTest extends AbstractNeo4jProducerTest {

    private NodeProducer nodeProducer;
    private Neo4jPredicateProducer predicateProducer;
    
    private OverridesPostProcessor postProcessor;
    private ClassNode declaringClass;
    private MethodNode declaringMethod;
    private ClassNode inTheMiddleClass;
    private MethodNode inTheMiddleMethod;
    private ClassNode overridingClass;
    private MethodNode overridingMethod;
    private ClassNode reOverridingClass;
    private MethodNode reOverridingMethod;

    @Before
    public void setUp() throws Exception {
        this.nodeProducer = new Neo4jNodeProducer(this.getGraphDb(), this.getEngine());
        this.predicateProducer = new Neo4jPredicateProducer(this.getGraphDb(), this.getEngine());
        this.postProcessor = new OverridesPostProcessor(this.getGraphDb(), this.getEngine());

        this.declaringClass = this.createClass("declaringClass");
        this.declaringMethod = this.createMethodNode("declaringMethod", "s");
        this.injectClassWithMethod(this.declaringClass, this.declaringMethod);

        this.inTheMiddleClass = this.createClass("inTheMiddleClass");
        
        this.overridingClass = this.createClass("overridingClass");
        this.overridingMethod = this.createMethodNode("overridingMethod", "s");
        this.injectClassWithMethod(this.overridingClass, this.overridingMethod);
        
        this.reOverridingClass = this.createClass("reOverridingClass");
        this.reOverridingMethod = this.createMethodNode("reOverridingMethod", "s");
        this.injectClassWithMethod(this.reOverridingClass, this.reOverridingMethod);
    }
    
    @Test
    public void testDirectExtends() throws Exception {
        this.predicateProducer.hasParent(extendsClass(this.overridingClass.getRef(), this.declaringClass.getRef()));
        
        this.postProcessor.process();

        this.assertUniqueRelationship(this.getMethodNode(this.overridingMethod), OVERRIDES, this.getMethodNode(this.declaringMethod));
    }
    
    @Test
    public void testIndirectExtends() throws Exception {
        this.predicateProducer.hasParent(extendsClass(this.inTheMiddleClass.getRef(), this.declaringClass.getRef()));
        this.predicateProducer.hasParent(extendsClass(this.overridingClass.getRef(), this.inTheMiddleClass.getRef()));
        
        this.postProcessor.process();

        this.assertUniqueRelationship(this.getMethodNode(this.overridingMethod), OVERRIDES, this.getMethodNode(this.declaringMethod));
    }


    @Ignore
    @Test
    public void testMultipleOverrideInHierarchy() throws Exception {
        this.predicateProducer.hasParent(extendsClass(this.overridingClass.getRef(), this.declaringClass.getRef()));
        this.predicateProducer.hasParent(extendsClass(this.reOverridingClass.getRef(), this.overridingClass.getRef()));
        
        this.postProcessor.process();

        this.assertUniqueRelationship(this.getMethodNode(this.overridingMethod), OVERRIDES, this.getMethodNode(this.declaringMethod));
        this.assertUniqueRelationship(this.getMethodNode(this.reOverridingMethod), OVERRIDES, this.getMethodNode(this.overridingMethod));
        this.assertNoSuchRelationship(this.getMethodNode(this.reOverridingMethod), OVERRIDES, this.getMethodNode(this.declaringMethod));
    }
    
    @Test
    public void testDirectImplements() throws Exception {
        this.predicateProducer.hasInterface(implementsInterface(this.overridingClass.getRef(), this.declaringClass.getRef()));
        
        this.postProcessor.process();

        this.assertUniqueRelationship(this.getMethodNode(this.overridingMethod), OVERRIDES, this.getMethodNode(this.declaringMethod));
    }
    
    @Test
    public void testImplementsExtends() throws Exception {
        this.predicateProducer.hasInterface(implementsInterface(this.inTheMiddleClass.getRef(), this.declaringClass.getRef()));
        this.predicateProducer.hasParent(extendsClass(this.overridingClass.getRef(), this.inTheMiddleClass.getRef()));
        
        this.postProcessor.process();

        this.assertUniqueRelationship(this.getMethodNode(this.overridingMethod), OVERRIDES, this.getMethodNode(this.declaringMethod));
    }
    
    @Test
    public void testExtendsImplements() throws Exception {
        this.predicateProducer.hasParent(extendsClass(this.inTheMiddleClass.getRef(), this.declaringClass.getRef()));
        this.predicateProducer.hasInterface(implementsInterface(this.overridingClass.getRef(), this.inTheMiddleClass.getRef()));
        
        this.postProcessor.process();

        this.assertUniqueRelationship(this.getMethodNode(this.overridingMethod), OVERRIDES, this.getMethodNode(this.declaringMethod));
    }

    private Node getMethodNode(MethodNode m1) {
        return this.assertUniqueNodeWithLabelAndName(Codec.Label.METHOD, m1.getRef().getName());
    }

    private MethodNode createMethodNode(String methodName, String signature) {
        MethodNode m1 = new MethodNode(new MethodRef("TEST", methodName));
        m1.getProperties().withSignature(MethodSignatureProperties.create().withSignature(signature));
        return m1;
    }

    private ClassNode createClass(String className) {
        return new ClassNode(new ClassRef("TEST", className));
    }

    private void injectClassWithMethod(ClassNode c1, MethodNode m1) throws ProducerException {
        this.nodeProducer.aClass(c1);
        this.nodeProducer.aMethod(m1);
        this.predicateProducer.hasMethod(Predicates.hasMethod(c1.getRef(), m1.getRef()));
    }
}
