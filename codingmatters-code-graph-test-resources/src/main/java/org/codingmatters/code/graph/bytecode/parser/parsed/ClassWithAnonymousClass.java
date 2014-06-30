package org.codingmatters.code.graph.bytecode.parser.parsed;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 30/06/14
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class ClassWithAnonymousClass {
    public void methodWithAnonymous() {
        new Runnable() {
            @Override
            public void run() {}
        };
    }
}
