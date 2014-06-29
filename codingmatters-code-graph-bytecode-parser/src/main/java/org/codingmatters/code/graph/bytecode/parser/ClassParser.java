package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;
import org.codingmatters.code.graph.bytecode.parser.asm.ByteCodeResolver;
import org.codingmatters.code.graph.bytecode.parser.asm.ClassParserVisitor;
import org.codingmatters.code.graph.bytecode.parser.exception.ClassParserException;
import org.codingmatters.code.graph.bytecode.parser.exception.ClassParsingError;
import org.objectweb.asm.ClassReader;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:16
 * To change this template use File | Settings | File Templates.
 */
public class ClassParser {
    private final NodeProducer nodeProducer;
    private final PredicateProducer predicateProducer;
    private final ClassParserVisitor visitor;
    private final ClassParserVisitor.ParsingErrorReporter errorReporter = new ClassParserVisitor.ParsingErrorReporter() {
        @Override
        public void report(ProducerException e) {
            parsingError(e);
        }
    };
    private final ByteCodeResolver resolver = new ByteCodeResolver() {
        @Override
        public ClassReader resolve(String name) throws IOException {
            return new ClassReader(name);
        }
    };

    public ClassParser(NodeProducer nodeProducer, PredicateProducer predicateProducer) {
        this.nodeProducer = nodeProducer;
        this.predicateProducer = predicateProducer;
        
        this.visitor = new ClassParserVisitor(this.nodeProducer, this.predicateProducer, this.errorReporter, resolver);
    }

    public void parse(Class aClass) throws ClassParserException {
        String name = aClass.getName();
        this.parse(name);
    }

    public void parse(String name) throws ClassParserException {
        try {
            this.resolver.resolve(name).accept(this.visitor, 0);
        } catch (IOException e) {
            throw new ClassParserException("error parsing class " + name, e);
        } catch(ClassParsingError e) {
            throw new ClassParserException("error parsing class " + name, e.getCause());
        }
    }


    private void parsingError(ProducerException e) {
        throw new ClassParsingError(e);
    }
}
