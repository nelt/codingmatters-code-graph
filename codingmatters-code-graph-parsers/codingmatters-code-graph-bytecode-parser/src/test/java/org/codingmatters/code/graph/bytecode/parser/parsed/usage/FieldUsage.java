package org.codingmatters.code.graph.bytecode.parser.parsed.usage;

/**
 * Created by nel on 13/03/15.
 */
public class FieldUsage {
    private String field1;
    private String field2 = this.field1;
    public void method() {
        this.field1 = "value";
        this.field2 = this.field1;
    }
}
