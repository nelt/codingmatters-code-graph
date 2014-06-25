package org.codingmatters.code.graph.bytecode.parser.parsed;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 23/06/14
 * Time: 20:51
 * To change this template use File | Settings | File Templates.
 */
public class ClassWithMethodInvokingMethod {
    
    public void method(ClassWithMethod c) throws Exception {
        c.method(null, null);
    }
    
}
