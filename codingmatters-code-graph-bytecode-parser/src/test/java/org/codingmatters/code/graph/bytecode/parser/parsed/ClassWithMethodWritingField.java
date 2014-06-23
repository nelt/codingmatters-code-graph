package org.codingmatters.code.graph.bytecode.parser.parsed;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 23/06/14
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
public class ClassWithMethodWritingField {
    public void method(ClassWithField c) {
        c.field = "abc";
    }
}
