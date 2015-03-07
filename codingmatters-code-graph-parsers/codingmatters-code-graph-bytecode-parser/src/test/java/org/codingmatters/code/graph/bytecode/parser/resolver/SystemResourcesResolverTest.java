package org.codingmatters.code.graph.bytecode.parser.resolver;

import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyClass;
import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.ClassReader;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 02/07/14
 * Time: 00:07
 * To change this template use File | Settings | File Templates.
 */
public class SystemResourcesResolverTest {
    private SystemResourcesResolver resolver = new SystemResourcesResolver();
    
    @Test
    public void testExistingClass() throws Exception {
        ClassReader actual = this.resolver.resolve(EmptyClass.class.getName());
        Assert.assertEquals(EmptyClass.class.getName().replace('.', '/'), actual.getClassName());
    }

    @Test(expected = IOException.class)
    public void testNotExistingClass() throws Exception {
        this.resolver.resolve("a.non.existing.Class");
    }
}
