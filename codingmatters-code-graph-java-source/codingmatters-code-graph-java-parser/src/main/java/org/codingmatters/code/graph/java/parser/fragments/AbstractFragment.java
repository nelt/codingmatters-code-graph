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
        private int start;
        private int end;

        public Builder withText(String text) {
            this.text = text;
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
                return clazz.getConstructor(new Class[]{String.class, Integer.class, Integer.class}).newInstance(this.text, this.start, this.end);
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
    private final int start;
    private final int end;

    public AbstractFragment(String text, Integer start, Integer end) {
        this.text = text;
        this.start = start;
        this.end = end;
    }

    @Override
    public String text() {
        return this.text;
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
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
