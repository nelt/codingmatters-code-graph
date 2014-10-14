package org.codingmatters.code.graph.ui.content.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Created by nel on 14/10/14.
*/
public class CompiledSubstitution {

    private final HashMap<String, Integer> indices = new HashMap<>();
    private final LinkedList<ToReplace> replacements = new LinkedList<ToReplace>();
    
    private final HashMap<String, Object> substitutions;
    private String compiled;

    public CompiledSubstitution(String format, HashMap<String, Object> substitutions) {
        this.substitutions = substitutions;
        this.compile(format);
    }

    /**
     *
     %[argument_index$][flags][width][.precision]conversion
     
     * @param format
     */
    static private Pattern FORMAT_PATTERN = Pattern.compile("%((\\w+)|(\\w+)(\\$[A-Za-z0-9.]+))%");

    private void compile(String format) {    
        Matcher m = FORMAT_PATTERN.matcher(format);

        while(m.find()) {
            String name = m.group(2) != null ? m.group(2) : m.group(3);
            if(! this.indices.containsKey(name)) {
                this.indices.put(name, this.indices.size() + 1);
            }
            if(m.group(2) != null) {
                this.replacements.add(new ToReplace("%" + this.indices.get(name) + "$s", m.start(), m.end()));
            } else {
                this.replacements.add(new ToReplace("%" + this.indices.get(name) + m.group(4), m.start(), m.end()));
            }
        }

        Collections.reverse(this.replacements);
        this.compiled = format;
        for (ToReplace replacement : this.replacements) {
            this.compiled = replacement.replaceIn(this.compiled);
        }
    }
    
    public String format() {
        return String.format(this.compiled, this.args());
    }

    private Object [] args() {
        Object [] args = new Object[indices.size()];
        for (String name : indices.keySet()) {
            args[indices.get(name) - 1] = this.substitutions.get(name);
        }
        return args;
    }


}
