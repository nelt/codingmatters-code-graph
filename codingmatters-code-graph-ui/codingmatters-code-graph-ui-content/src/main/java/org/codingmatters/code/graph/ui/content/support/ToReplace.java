package org.codingmatters.code.graph.ui.content.support;

/**
* Created by nel on 14/10/14.
*/
public class ToReplace {
    private final String replacement;
    private final int start;
    private final int end;

    public ToReplace(String replacement, int start, int end) {
        this.replacement = replacement;
        this.start = start;
        this.end = end;
    }

    public String replaceIn(String original) {
        StringBuilder result = new StringBuilder();
        if(this.start > 0) {
            result.append(original.substring(0, this.start));
        }
        result.append(this.replacement);
        if(this.end < original.length()) {
            result.append(original.substring(this.end));
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return "ToReplace{" +
                "replacement='" + replacement + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
