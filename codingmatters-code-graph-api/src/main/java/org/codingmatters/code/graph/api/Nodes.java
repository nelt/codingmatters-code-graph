package org.codingmatters.code.graph.api;

import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:05
 * To change this template use File | Settings | File Templates.
 */
public final class Nodes {
    static public ClassNode classNode(ClassRef ref) {
        return new ClassNode(ref);
    }
    
    static public MethodNode methodNode(MethodRef ref) {
        return new MethodNode(ref);
    }
    
    static public FieldNode fieldNode(FieldRef ref) {
        return new FieldNode(ref);
    }
}
