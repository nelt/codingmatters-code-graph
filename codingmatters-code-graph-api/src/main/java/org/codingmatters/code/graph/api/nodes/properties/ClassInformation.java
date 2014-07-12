package org.codingmatters.code.graph.api.nodes.properties;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 12/07/14
 * Time: 03:50
 * To change this template use File | Settings | File Templates.
 */
public class ClassInformation {
    
    static public Builder create() {
        return new Builder();
    }
    
    static public class Builder {
        private String className;

        public Builder withClassName(String className) {
            this.className = className;
            return this;
        }
        
        public ClassInformation build() {
            return new ClassInformation(this.className);
        }
    }
    
    private final String className;

    private ClassInformation(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassInformation that = (ClassInformation) o;

        if (className != null ? !className.equals(that.className) : that.className != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return className != null ? className.hashCode() : 0;
    }
}
