package org.codingmatters.code.graph.api.references;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:08
 * To change this template use File | Settings | File Templates.
 */
public class ClassRef extends AbstractRef {

    public ClassRef(String shortName) {
        super(shortName);
    }

    public ClassRef(String source, String shortName) {
        super(source, shortName);
    }
    
}
