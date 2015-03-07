package org.codingmatters.code.graph.java.parser.fragments;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 22/07/14
 * Time: 07:11
 * To change this template use File | Settings | File Templates.
 */
public interface SourceFragment {
    String text();
    String qualifiedName();
    int start();
    int end();
}
