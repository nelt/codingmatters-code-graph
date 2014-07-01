package org.codingmatters.code.graph.api.references;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public class MethodRef extends AbstractRef implements UsableRef {
    public MethodRef(String shortName) {
        super(shortName);
    }

    public MethodRef(String source, String shortName) {
        super(source, shortName);
    }
}
