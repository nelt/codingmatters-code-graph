package org.codingmatters.code.graph.bytecode.parser.resolver;

import org.codingmatters.code.graph.bytecode.parser.asm.ByteCodeResolver;
import org.objectweb.asm.ClassReader;

import java.io.Closeable;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 02/07/14
 * Time: 00:36
 * To change this template use File | Settings | File Templates.
 */
public class JarResolver implements ByteCodeResolver, Closeable {
    private final JarFile jar;

    public JarResolver(JarFile jar) {
        this.jar = jar;
    }

    @Override
    public ClassReader resolve(String name) throws IOException {
        JarEntry entry = this.jar.getJarEntry(name.replace('.', '/') + ".class");
        if(entry == null) {
            throw new IOException("no such class " + name + "in jar file");
        }
        return new ClassReader(this.jar.getInputStream(entry));
    }

    @Override
    public void close() throws IOException {
        this.jar.close();
    }
}
