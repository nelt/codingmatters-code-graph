package org.codingmatters.code.graph.storage.neo4j.internal;

import org.neo4j.graphdb.Label;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/06/14
 * Time: 01:33
 * To change this template use File | Settings | File Templates.
 */
public interface Codec {
    enum Label implements org.neo4j.graphdb.Label {
        CLASS, FIELD, METHOD       
    }
}
