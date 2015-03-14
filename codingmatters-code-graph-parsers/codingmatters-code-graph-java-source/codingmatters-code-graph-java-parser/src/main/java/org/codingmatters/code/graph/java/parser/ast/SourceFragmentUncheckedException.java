package org.codingmatters.code.graph.java.parser.ast;

/**
 * Created by nel on 25/02/15.
 */
public class SourceFragmentUncheckedException extends RuntimeException {
    public SourceFragmentUncheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceFragmentUncheckedException(String message) {
        super(message);
    }
}
