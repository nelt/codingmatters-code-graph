package org.codingmatters.code.graph.java.parser.ast;

/**
 * Created by nel on 03/03/15.
 */
public enum PrimitiveType {
    INT("I"),
    LONG("J"),
    FLOAT("F"),
    DOUBLE("D"),
    CHAR("C"),
    BOOLEAN("Z"),
    BYTE("B"),
    SHORT("S")
    ;

    static public boolean isPrimitive(String name) {
        return forType(name) != null;
    }

    static public PrimitiveType forType(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private final String spec;

    PrimitiveType(String spec) {
        this.spec = spec;
    }

    public String getSpec() {
        return spec;
    }
}
