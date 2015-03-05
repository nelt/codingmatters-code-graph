package org.test;
/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 19/07/14
 * Time: 06:29
 * To change this template use File | Settings | File Templates.
 */
public class TestClass {
    static final String CONSTANT;
    private String initializedField = CONSTANT;
    
    public String getter() {
        return initializedField;
    }
    public String getterUsingThis() {
        return this.initializedField;
    }
}