package org.codingmatters.code.graph.bytecode.parser.util;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 19/07/14
 * Time: 06:29
 * To change this template use File | Settings | File Templates.
 */
public class TestClass {
    
    static public class InnerStaticClass {
        
    }
    
    static private Runnable run = new Runnable() {
        @Override
        public void run() {
            
        }
    };
    
    private final String field;

    public TestClass(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
    
    public String method(String arg) throws Exception {
        Date d = new Date();
        return d.toString();
    }
}
