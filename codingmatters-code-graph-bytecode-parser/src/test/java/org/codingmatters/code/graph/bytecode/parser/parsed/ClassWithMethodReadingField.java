package org.codingmatters.code.graph.bytecode.parser.parsed;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 23/06/14
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */
public class ClassWithMethodReadingField {
    
    public void method(ClassWithField c) {
        String value = c.field;
    }
    
}
