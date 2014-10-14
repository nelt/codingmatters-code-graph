package org.codingmatters.code.graph.ui.content.support;

import java.util.HashMap;

/**
* Created by nel on 14/10/14.
*/
class ConcreteSubstitution implements Substitutions.Substitution, Substitutions.Value {
    private String pendingSubstitution;
    private final HashMap<String, Object> substitutions = new HashMap<>();
    
    @Override
    public Substitutions.Value replace(String name) {
        this.pendingSubstitution = name;
        return this;
    }

    @Override
    public Substitutions.Substitution with(Object value) {
        this.substitutions.put(this.pendingSubstitution, value);
        return this;
    }

    @Override
    public String in(String format) {
        return new CompiledSubstitution(format, this.substitutions).format();
    }

}
