package org.codingmatters.code.graph.api.producer;

import org.codingmatters.code.graph.api.nodes.ClassNode;
import org.codingmatters.code.graph.api.nodes.FieldNode;
import org.codingmatters.code.graph.api.nodes.MethodNode;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
public interface NodeProducer {
    void aClass(ClassNode node) throws ProducerException;
    void aField(FieldNode node) throws ProducerException;
    void aMethod(MethodNode node) throws ProducerException;
}
