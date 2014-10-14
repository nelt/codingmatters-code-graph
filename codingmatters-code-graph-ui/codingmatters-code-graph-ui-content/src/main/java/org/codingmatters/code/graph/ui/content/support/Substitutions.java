package org.codingmatters.code.graph.ui.content.support;

/**
 * Created by nel on 14/10/14.
 */
public class Substitutions {
    
    static public Value replace(String name) {
        return new ConcreteSubstitution().replace(name);    
    }

    static public interface Substitution {
        Value replace(String name);
        String in(String format);
    }

    static public interface Value {
        Substitution with(Object vale);
    }


}
