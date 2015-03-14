package org.codingmatters.code.graph.java.parser.ast;

/**
 * Created by nel on 25/07/14.
 */
public interface ClassDisambiguizer {
    String choosePackage(String forClass, String [] orderedCandidates) throws DisambiguizerException;
}
