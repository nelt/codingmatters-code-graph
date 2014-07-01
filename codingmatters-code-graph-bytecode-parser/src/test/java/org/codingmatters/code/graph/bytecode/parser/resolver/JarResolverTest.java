package org.codingmatters.code.graph.bytecode.parser.resolver;

import org.codingmatters.code.graph.bytecode.parser.parsed.EmptyClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 02/07/14
 * Time: 00:42
 * To change this template use File | Settings | File Templates.
 */
public class JarResolverTest {
    private JarResolver resolver;

    @Before
    public void setUp() throws Exception {
        this.resolver = new JarResolver(new JarFile(ClassResourcesHelper.makeTemporaryJar(EmptyClass.class)));
    }
    
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
