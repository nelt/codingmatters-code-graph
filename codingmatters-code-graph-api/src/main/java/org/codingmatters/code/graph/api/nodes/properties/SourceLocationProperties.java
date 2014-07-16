package org.codingmatters.code.graph.api.nodes.properties;

import org.codingmatters.code.graph.api.nodes.properties.annotations.Storable;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 12/07/14
 * Time: 03:47
 * To change this template use File | Settings | File Templates.
 */
public class SourceLocationProperties {
    
    static public Builder create() {
        return new Builder();
    }
    
    static public class Builder {
        private String source;
        private Integer line;
        private Integer startColumn;
        private Integer endColumn;

        public Builder withLine(Integer line) {
            this.line = line;
            return this;
        }

        public Builder withStartColumn(Integer startColumn) {
            this.startColumn = startColumn;
            return this;
        }

        public Builder withEndColumn(Integer endColumn) {
            this.endColumn = endColumn;
            return this;
        }
        
        
        public Builder withSource(String source) {
            this.source = source;
            return this;
        }
        
        
        
        public SourceLocationProperties build() {
            return new SourceLocationProperties(this.source, this.line, this.startColumn, this.endColumn);
        }
    }
    
    private final String source;
    private final Integer line;
    private final Integer startColumn;
    private final Integer endColumn;

    private SourceLocationProperties(String source, Integer line, Integer startColumn, Integer endColumn) {
        this.source = source;
        this.line = line;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
    }

    @Storable
    public Integer getLine() {
        return line;
    }
    
    @Storable
    public Integer getStartColumn() {
        return startColumn;
    }
    
    @Storable
    public Integer getEndColumn() {
        return endColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceLocationProperties that = (SourceLocationProperties) o;

        if (endColumn != null ? !endColumn.equals(that.endColumn) : that.endColumn != null) return false;
        if (line != null ? !line.equals(that.line) : that.line != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (startColumn != null ? !startColumn.equals(that.startColumn) : that.startColumn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (line != null ? line.hashCode() : 0);
        result = 31 * result + (startColumn != null ? startColumn.hashCode() : 0);
        result = 31 * result + (endColumn != null ? endColumn.hashCode() : 0);
        return result;
    }
}
