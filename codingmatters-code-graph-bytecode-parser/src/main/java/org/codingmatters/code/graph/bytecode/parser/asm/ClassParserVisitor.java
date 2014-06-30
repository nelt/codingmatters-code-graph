package org.codingmatters.code.graph.bytecode.parser.asm;

import org.codingmatters.code.graph.api.Nodes;
import org.codingmatters.code.graph.api.Predicates;
import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.predicates.ExtendsPredicate;
import org.codingmatters.code.graph.api.predicates.HasFieldPredicate;
import org.codingmatters.code.graph.api.predicates.HasMethodPredicate;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.objectweb.asm.*;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public class ClassParserVisitor extends ClassVisitor {
    private final NodeProducer nodeProducer;
    private final PredicateProducer predicateProducer;
    private final ParsingErrorReporter errorReporter;
    private ClassNode currentClassNode;
    private final ByteCodeResolver resolver;
    
    public ClassParserVisitor(NodeProducer nodeProducer, PredicateProducer predicateProducer, ParsingErrorReporter errorReporter, ByteCodeResolver resolver) {
        super(Opcodes.ASM5);
        this.nodeProducer = nodeProducer;
        this.predicateProducer = predicateProducer;
        this.errorReporter = errorReporter;
        this.resolver = resolver;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        try {
            this.currentClassNode = Nodes.classNode(new ClassRef(name));
            this.nodeProducer.aClass(this.currentClassNode);
            this.predicateProducer.hasParent(new ExtendsPredicate(this.currentClassNode.getRef(), new ClassRef(superName)));
        } catch (ProducerException e) {
            this.errorReporter.report(e);
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }


    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        try {
            FieldRef fieldRef = new FieldRef(NameUtil.fieldName(this.currentClassNode.getRef().getName(), name));
            this.nodeProducer.aField(new FieldNode(fieldRef));
            this.predicateProducer.hasField(new HasFieldPredicate(new ClassRef(this.currentClassNode.getRef().getName()), fieldRef));
        } catch (ProducerException e) {
            this.errorReporter.report(e);
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodRef methodRef = new MethodRef(NameUtil.methodName(this.currentClassNode.getRef().getName(), name, desc));
        try {
            this.nodeProducer.aMethod(new MethodNode(methodRef));
            this.predicateProducer.hasMethod(new HasMethodPredicate(new ClassRef(this.currentClassNode.getRef().getName()), methodRef));
        } catch (ProducerException e) {
            this.errorReporter.report(e);
        }
        return new MethodParserVisitor(methodRef, this.predicateProducer, this.errorReporter);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        if(name.equals(this.currentClassNode.getRef().getName())) return;
        try {
            ClassReader classReader = this.resolver.resolve(name.replaceAll("/", "."));
            classReader.accept(new ClassParserVisitor(this.nodeProducer, this.predicateProducer, this.errorReporter, resolver), 0);
            
            ClassNode innerClassNode = Nodes.classNode(new ClassRef(name));
            this.predicateProducer.hasInner(Predicates.hasInner(this.currentClassNode.getRef(), innerClassNode.getRef()));
        } catch (ProducerException e) {
            this.errorReporter.report(e);
        } catch (IOException e) {
            this.errorReporter.report(new ProducerException("error resolving inner class " + outerName, e));
        }
    }

    static public interface ParsingErrorReporter {
        void report(ProducerException e);
    }
}
