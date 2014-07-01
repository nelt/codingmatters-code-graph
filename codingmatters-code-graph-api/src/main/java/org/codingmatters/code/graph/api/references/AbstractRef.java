package org.codingmatters.code.graph.api.references;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 02/07/14
 * Time: 01:22
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractRef implements Ref {
    
    private final String source;
    private final String shortName;

    public AbstractRef(String shortName) {
        this(null, shortName);
    }
    
    public AbstractRef(String source, String shortName) {
        this.source = source;
        this.shortName = shortName;
    }

    @Override
    public String getName() {
        if(this.getSource() != null) {
            return this.getSource() + ":" + this.getShortName();    
        } else {
            return this.getShortName();
        }
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public String getShortName() {
        return this.shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractRef that = (AbstractRef) o;

        if (shortName != null ? !shortName.equals(that.shortName) : that.shortName != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "name='" + this.getName() + '\'' +
                '}';
    }
}
