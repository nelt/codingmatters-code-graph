package org.codingmatters.code.graph.bytecode.parser.parsed.usage;

/**
 * Created by nel on 13/03/15.
 */
public class FieldUsage {
    private String used;
    private String user = used;
    public void method() {
        this.used = "written";
    }
}
