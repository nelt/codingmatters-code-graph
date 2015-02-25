package org.codingmatters.code.graph.java.parser.internal;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by nel on 25/02/15.
 */
public class InnerClassCounter {
    
    private final Stack<AtomicInteger> innerClassCounters = new Stack<>();

    public void next() {
        this.innerClassCounters.push(new AtomicInteger(0));
    }

    public void previous() {
        this.innerClassCounters.pop();
    }

    public int currentNextValue() {
        return this.innerClassCounters.peek().incrementAndGet();
    }
}
