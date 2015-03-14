package org.codingmatters.code.graph.java.parser.ast;

import java.util.Arrays;

/**
 * Created by nel on 25/07/14.
 */
public class ClassDisambiguizerCall {
    private final String forClass;
    private final String [] orderedCandidates;

    public ClassDisambiguizerCall(String forClass, String[] orderedCandidates) {
        this.forClass = forClass;
        this.orderedCandidates = orderedCandidates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassDisambiguizerCall that = (ClassDisambiguizerCall) o;

        if (forClass != null ? !forClass.equals(that.forClass) : that.forClass != null) return false;
        if (!Arrays.equals(orderedCandidates, that.orderedCandidates)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = forClass != null ? forClass.hashCode() : 0;
        result = 31 * result + (orderedCandidates != null ? Arrays.hashCode(orderedCandidates) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClassDisambiguizerCall{" +
                "forClass='" + forClass + '\'' +
                ", orderedCandidates=" + Arrays.toString(orderedCandidates) +
                '}';
    }
}
