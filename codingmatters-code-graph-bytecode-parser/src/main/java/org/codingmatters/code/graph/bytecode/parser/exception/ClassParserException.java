package org.codingmatters.code.graph.bytecode.parser.exception;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 15:00
 * To change this template use File | Settings | File Templates.
 */
public class ClassParserException extends Exception {
    public ClassParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
