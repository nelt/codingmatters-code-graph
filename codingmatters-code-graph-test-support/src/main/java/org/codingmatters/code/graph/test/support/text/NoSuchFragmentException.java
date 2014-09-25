package org.codingmatters.code.graph.test.support.text;

import java.util.NoSuchElementException;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/09/14
 * Time: 22:16
 * To change this template use File | Settings | File Templates.
 */
public class NoSuchFragmentException extends NoSuchElementException {
    public NoSuchFragmentException(String s) {
        super(s);
    }
}
