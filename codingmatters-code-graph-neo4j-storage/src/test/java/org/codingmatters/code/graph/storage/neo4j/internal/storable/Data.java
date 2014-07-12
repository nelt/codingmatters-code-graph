package org.codingmatters.code.graph.storage.neo4j.internal.storable;

import org.codingmatters.code.graph.api.nodes.properties.annotations.StorableProperties;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 12/07/14
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
public class Data {
    
    private Props storable;
    private Props notStorable;
    
    @StorableProperties    
    public Props getStorable() {
        return storable;
    }

    public Data withStorable(Props storable) {
        this.storable = storable;
        return this;
    }

    public Props getNotStorable() {
        return notStorable;
    }

    public Data withNotStorable(Props notStorable) {
        this.notStorable = notStorable;
        return this;
    }
}
