package org.codingmatters.code.graph.api.nodes.properties;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 12/07/14
 * Time: 03:47
 * To change this template use File | Settings | File Templates.
 */
public class SourceLocation {
    
    static public Builder create() {
        return new Builder();
    }
    
    static public class Builder {

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
        
        public SourceLocation build() {
            return new SourceLocation(this.line, this.startColumn, this.endColumn);
        }
    }
    
    private final Integer line;
    private final Integer startColumn;
    private final Integer endColumn;

    private SourceLocation(Integer line, Integer startColumn, Integer endColumn) {
        this.line = line;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
    }

    public Integer getLine() {
        return line;
    }

    public Integer getStartColumn() {
        return startColumn;
    }

    public Integer getEndColumn() {
        return endColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceLocation that = (SourceLocation) o;

        if (endColumn != null ? !endColumn.equals(that.endColumn) : that.endColumn != null) return false;
        if (line != null ? !line.equals(that.line) : that.line != null) return false;
        if (startColumn != null ? !startColumn.equals(that.startColumn) : that.startColumn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = line != null ? line.hashCode() : 0;
        result = 31 * result + (startColumn != null ? startColumn.hashCode() : 0);
        result = 31 * result + (endColumn != null ? endColumn.hashCode() : 0);
        return result;
    }
}
