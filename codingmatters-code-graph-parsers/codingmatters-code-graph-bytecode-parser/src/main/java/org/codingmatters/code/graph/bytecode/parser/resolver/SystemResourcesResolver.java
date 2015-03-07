package org.codingmatters.code.graph.bytecode.parser.resolver;

import org.codingmatters.code.graph.bytecode.parser.asm.ByteCodeResolver;
import org.objectweb.asm.ClassReader;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 01/07/14
 * Time: 06:35
 * 
 * Resolves the class from system resources
 * 
 */
public class SystemResourcesResolver implements ByteCodeResolver {
    @Override
    public ClassReader resolve(String name) throws IOException {
        return new ClassReader(name);
    }
}
