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

     The optional argument_index is a decimal integer indicating the position of the argument in the argument list. 
     The first argument is referenced by "1$", the second by "2$", etc.

     The optional flags is a set of characters that modify the output format. The set of valid flags depends on the conversion.

     The optional width is a non-negative decimal integer indicating the minimum number of characters to be written to the output.

     The optional precision is a non-negative decimal integer usually used to restrict the number of characters. 
     The specific behavior depends on the conversion.

     The required conversion is a character indicating how the argument should be formatted. 
     The set of valid conversions for a given argument depends on the argument's data type.
     
     * @param format
     */
    private void compile(String format) {
        Pattern p = Pattern.compile("(%(\\w+)%)|(%(\\w+)\\$\\w+%)");
        Matcher m = p.matcher(format);

        while(m.find()) {
            String name = m.group(2);
            if(! this.indices.containsKey(name)) {
                this.indices.put(name, this.indices.size() + 1);
            }
            this.replacements.add(new ToReplace("%" + this.indices.get(name) + "$s", m.start(), m.end()));
        }

        Collections.reverse(this.replacements);
        this.compiled = format;
        for (ToReplace replacement : this.replacements) {
            this.compiled = replacement.replaceIn(this.compiled);
        }
        System.out.println(this.compiled);
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
