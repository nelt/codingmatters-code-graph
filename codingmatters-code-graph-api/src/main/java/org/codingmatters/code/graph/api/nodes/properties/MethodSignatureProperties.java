package org.codingmatters.code.graph.api.nodes.properties;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 16/07/14
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class MethodSignatureProperties {

    static public Builder create() {
        return new Builder();
    }
    
    static public class Builder {
        private String signature;

        public Builder withSignature(String signature) {
            this.signature = signature;
            return this;
        }
        
        public MethodSignatureProperties build() {
            return new MethodSignatureProperties(this.signature);
        }
    }
    
    private final String signature;

    public MethodSignatureProperties(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodSignatureProperties that = (MethodSignatureProperties) o;

        if (signature != null ? !signature.equals(that.signature) : that.signature != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return signature != null ? signature.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MehodSignatureProperties{" +
                "signature='" + signature + '\'' +
                '}';
    }
}
