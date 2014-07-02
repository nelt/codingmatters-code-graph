package org.codingmatters.code.graph.bytecode.parser.asm;

import org.codingmatters.code.graph.api.predicates.UsesPredicate;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.api.references.FieldRef;
import org.codingmatters.code.graph.api.references.MethodRef;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 23/06/14
 * Time: 10:12
 * To change this template use File | Settings | File Templates.
 */
public class MethodParserVisitor extends MethodVisitor {

    private final MethodRef methodRef;
    private final PredicateProducer predicateProducer;
    private final ClassParserVisitor.ParsingErrorReporter errorReporter;
    private final String source;
    private int currentLine;

    public MethodParserVisitor(MethodRef methodRef, PredicateProducer predicateProducer, ClassParserVisitor.ParsingErrorReporter errorReporter, String source) {
        super(Opcodes.ASM5);
        this.methodRef = methodRef;
        this.predicateProducer = predicateProducer;
        this.errorReporter = errorReporter;
        this.source = source;
    }


    @Override
    public void visitLineNumber(int line, Label start) {
        this.currentLine = line;
        super.visitLineNumber(line, start);
    }
    
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        if(Opcodes.GETFIELD == opcode) {
            try {
                this.predicateProducer.usage(new UsesPredicate(this.methodRef, new FieldRef(this.source, fieldName(owner, name))));
            } catch (ProducerException e) {
                this.errorReporter.report(e);
            }
        } else if(Opcodes.PUTFIELD == opcode) {
            try {
                this.predicateProducer.usage(new UsesPredicate(this.methodRef, new FieldRef(this.source, fieldName(owner, name))));
            } catch (ProducerException e) {
                this.errorReporter.report(e);
            }
        }
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    private String fieldName(String owner, String name) {
        return NameUtil.fieldName(owner, name);
    }
    
    

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        try {
            this.predicateProducer.usage(
                    new UsesPredicate(
                            this.methodRef, 
                            new MethodRef(this.source, NameUtil.methodName(owner, name, desc))
                    )
            );
        } catch (ProducerException e) {
            this.errorReporter.report(e);
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }
    
}
