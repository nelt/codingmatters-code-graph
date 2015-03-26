package org.codingmatters.code.graph.java.parser.fragments;

import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 22/07/14
 * Time: 07:13
 * To change this template use File | Settings | File Templates.
 */
public class AbstractFragment implements SourceFragment {

    static public class Builder {

        private String text;
        private String qualifiedName;
        private int start;
        private int end;

        public Builder withText(String text) {
            this.text = text;
            return this;
        }

        public Builder withQualifiedName(String qualifiedName) {
            this.qualifiedName = qualifiedName;
            return this;
        }

        public Builder withStart(int start) {
            this.start = start;
            return this;
        }

        public Builder withEnd(int end) {
            this.end = end;
            return this;
        }

        public <T extends AbstractFragment> T build(Class<T> clazz) throws BuilderException {
            try {
                return clazz.getConstructor(new Class[]{String.class, String.class, Integer.class, Integer.class})
                        .newInstance(this.text, this.qualifiedName, this.start, this.end);
            } catch (InstantiationException|IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
                throw new BuilderException(e);
            }
        }

        public class BuilderException extends Exception {
            private BuilderException(Throwable cause) {
                super(cause);
            }
        }
    }
    
    private final String text;
    private final String qualifiedName;
    private final int start;
    private final int end;

    public AbstractFragment(String text, String qualifiedName, Integer start, Integer end) {
        this.text = text;
        this.qualifiedName = qualifiedName;
        this.start = start;
        this.end = end;
    }

    @Override
    public String text() {
        return this.text;
    }

    public String qualifiedName() {
        return qualifiedName;
    }

    @Override
    public int start() {
        return this.start;
    }

    @Override
    public int end() {
        return this.end;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "text='" + text + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractFragment that = (AbstractFragment) o;

        if (start != that.start) return false;
        if (end != that.end) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return !(qualifiedName != null ? !qualifiedName.equals(that.qualifiedName) : that.qualifiedName != null);

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (qualifiedName != null ? qualifiedName.hashCode() : 0);
        result = 31 * result + start;
        result = 31 * result + end;
        return result;
    }
}
