package org.codingmatters.code.graph.bytecode.parser.util;

import java.util.Date;
import java.util.concurrent.atomic.*;

import org.codingmatters.code.graph.Foo;
import org.codingmatters.code.graph.Bar;
import org.codingmatters.code.graph.Baz;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 19/07/14
 * Time: 06:29
 * To change this template use File | Settings | File Templates.
 */
public class TestClass {
    
    static public class InnerStaticClass {
        class InnerInner {
        }
    }
    
    static private Runnable run = new Runnable() {
        @Override
        public void run() {
        }
    };
    
    private final Foo field;
    private Bar dd;

    public TestClass(Foo field) {
        this.field = field;
    }

    public Baz getField() {
        return field.asBaz();
    }
    
    public String method(String arg) throws Exception {
        Date d = new Date();
        return d.toString();
    }
}
