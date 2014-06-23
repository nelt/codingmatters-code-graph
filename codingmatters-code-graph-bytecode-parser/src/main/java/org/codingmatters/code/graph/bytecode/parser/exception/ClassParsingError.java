package org.codingmatters.code.graph.bytecode.parser.exception;

import org.codingmatters.code.graph.api.producer.exception.ProducerException;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class ClassParsingError extends RuntimeException {
    public ClassParsingError(ProducerException e) {
        super(e);
    }
}
