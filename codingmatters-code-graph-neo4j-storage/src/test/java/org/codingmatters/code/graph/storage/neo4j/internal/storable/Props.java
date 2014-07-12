package org.codingmatters.code.graph.storage.neo4j.internal.storable;

import org.codingmatters.code.graph.api.nodes.properties.annotations.Storable;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 12/07/14
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
public class Props {
    
    static public class Builder {
        private String field1;
        private String field2;

        public Builder withField1(String str) {
            this.field1 = str;
            return this;
        }

        public Builder withField2(String field2) {
            this.field2 = field2;
            return this;
        }

        public Props build() {
            return new Props(this.field1, this.field2);
        }
    }
    
    private final String field1;
    private final String field2;

    private Props(String field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    @Storable
    public String getField1() {
        return field1;
    }

    @Storable
    public String getField2() {
        return field2;
    }

    public Builder builder() {
        return new Builder()
                .withField1(this.getField1())
                .withField2(this.getField2());
    }
}
