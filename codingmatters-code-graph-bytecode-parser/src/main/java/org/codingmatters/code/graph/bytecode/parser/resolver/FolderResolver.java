package org.codingmatters.code.graph.bytecode.parser.resolver;

import org.codingmatters.code.graph.bytecode.parser.asm.ByteCodeResolver;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 01/07/14
 * Time: 06:39
 * To change this template use File | Settings | File Templates.
 */
public class FolderResolver implements ByteCodeResolver {
    private final File resolverRoot;

    public FolderResolver(File resolverRoot) {
        this.resolverRoot = resolverRoot;
    }

    @Override
    public ClassReader resolve(String name) throws IOException {
        File classFile = new File(this.resolverRoot, name.replace('.', '/') + ".class");
        return new ClassReader(new FileInputStream(classFile));
    }
}
