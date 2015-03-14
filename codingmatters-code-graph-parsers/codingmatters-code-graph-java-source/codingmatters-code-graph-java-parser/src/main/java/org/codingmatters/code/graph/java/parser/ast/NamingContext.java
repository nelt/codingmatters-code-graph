package org.codingmatters.code.graph.java.parser.ast;

import java.util.Stack;

/**
 * Created by nel on 25/02/15.
 */
public class NamingContext {

    private final Stack<String> namingStack = new Stack<>();

    void next(String name) {
        this.namingStack.push(name);
    }

    void previous() {
        this.namingStack.pop();
    }

    String current() {
        return namingStack.peek();
    }
}
