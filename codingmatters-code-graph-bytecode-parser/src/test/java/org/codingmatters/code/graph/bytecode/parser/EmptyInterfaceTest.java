package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.Nodes;
import org.codingmatters.code.graph.api.Predicates;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyImplementerClass;
import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyInterface;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 16/07/14
 * Time: 07:01
 * To change this template use File | Settings | File Templates.
 */
public class EmptyInterfaceTest extends AbstractClassParserTest {

    public static final ClassRef INTERFACE_REF = new ClassRef(className(EmptyInterface.class));
    public static final ClassRef OBJECT_REF = new ClassRef(className(Object.class));
    public static final ClassRef IMPLEMENTER_REF = new ClassRef(className(EmptyImplementerClass.class));

    @Test
    public void testInterface() throws Exception {
        this.getParser().parse(EmptyInterface.class);
        
        assertProducedExactly(
                Nodes.classNode(INTERFACE_REF),
                Predicates.extendsClass(INTERFACE_REF, OBJECT_REF)
        );
    }

    @Test
    public void testImplementer() throws Exception {
        this.getParser().parse(EmptyImplementerClass.class);
        
        assertProduced(Predicates.implementsInterface(IMPLEMENTER_REF, INTERFACE_REF));
    }
}
