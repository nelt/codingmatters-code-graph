package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.nodes.properties.ClassInformationProperties;
import org.codingmatters.code.graph.api.nodes.properties.MethodSignatureProperties;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.ClassWithMethod;
import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyClass;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;
import static org.fest.assertions.api.Assertions.filter;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 12/07/14
 * Time: 04:10
 * To change this template use File | Settings | File Templates.
 */
public class NodePropertiesTest extends AbstractClassParserTest {

    @Override
    protected PredicateProducer getPredicateProducer() {
        return NOOP_PREDICATE_PRODUCER;
    }

    @Test
    public void testClassNameProperty() throws Exception {
        this.getParser().parse(EmptyClass.class);
        
        assertThat(
                extractProperty("properties.information").from(filterClassNodes(this.getProduced()).get()))
                .containsExactly(
                        ClassInformationProperties.create().withClassName(EmptyClass.class.getName()).build()
                );
    }

    @Test
    public void testMethodSignature() throws Exception {
        this.getParser().parse(ClassWithMethod.class);
        assertThat(
                extractProperty("properties.signature").from(filterMethodNodes(this.getProduced(), className(ClassWithMethod.class) + "#method(Ljava/lang/Integer;Ljava/util/List;)Ljava/lang/String;").get()))
                .contains(
                        MethodSignatureProperties.create().withSignature(
                                "method(Ljava/lang/Integer;Ljava/util/List;)Ljava/lang/String;"
                        ).build()
                );
    }
}
