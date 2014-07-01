package org.codingmatters.code.graph.api.references;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:09
 * To change this template use File | Settings | File Templates.
 */
public class FieldRef extends AbstractRef implements UsableRef {

    public FieldRef(String shortName) {
        super(shortName);
    }

    public FieldRef(String source, String shortName) {
        super(source, shortName);
    }
}
