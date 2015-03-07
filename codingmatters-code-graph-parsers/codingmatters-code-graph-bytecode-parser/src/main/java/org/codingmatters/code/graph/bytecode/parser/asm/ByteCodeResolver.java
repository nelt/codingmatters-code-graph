package org.codingmatters.code.graph.bytecode.parser.asm;

import org.objectweb.asm.ClassReader;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 29/06/14
 * Time: 15:03
 * To change this template use File | Settings | File Templates.
 */
public interface ByteCodeResolver {
    ClassReader resolve(String name) throws IOException;
}
