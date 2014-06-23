package org.codingmatters.code.graph.bytecode.parser.asm;

import org.codingmatters.code.graph.api.Nodes;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.api.references.ClassRef;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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
    private String currentClassName;

    public ClassParserVisitor(NodeProducer nodeProducer, PredicateProducer predicateProducer, ParsingErrorReporter errorReporter) {
        super(Opcodes.ASM5);
        this.nodeProducer = nodeProducer;
        this.predicateProducer = predicateProducer;
        this.errorReporter = errorReporter;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.currentClassName = this.className(name);
        try {
            this.nodeProducer.aClass(Nodes.classNode(new ClassRef(this.currentClassName)));
        } catch (ProducerException e) {
            this.errorReporter.report(e);
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }


    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        try {
            this.nodeProducer.aField(new FieldNode(new FieldRef(this.fieldName(this.currentClassName, name))));
        } catch (ProducerException e) {
            this.errorReporter.report(e);
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        try {
            this.nodeProducer.aMethod(new MethodNode(new MethodRef(this.methodName(this.currentClassName, name))));
        } catch (ProducerException e) {
            this.errorReporter.report(e);
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    private String methodName(String currentClassName, String rawMethodName) {
        return currentClassName + "#" + rawMethodName + "()";
    }

    private String fieldName(String className, String rawFieldName) {
        return className + "#" + rawFieldName;
    }

    private String className(String name) {
        return name.replaceAll("/", ".");
    }

    static public interface ParsingErrorReporter {
        void report(ProducerException e);
    }
}
