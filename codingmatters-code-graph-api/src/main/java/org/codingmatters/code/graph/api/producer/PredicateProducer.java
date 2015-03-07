package org.codingmatters.code.graph.api.producer;

import org.codingmatters.code.graph.api.predicates.*;
import org.codingmatters.code.graph.api.producer.exception.ProducerException;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 21/06/14
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public interface PredicateProducer {
    void hasParent(ExtendsPredicate predicate) throws ProducerException;
    void hasField(HasFieldPredicate predicate) throws ProducerException;
    void hasMethod(HasMethodPredicate predicate) throws ProducerException;
    void hasInner(HasInnerClassPredicate predicate) throws ProducerException;
    void usage(UsesPredicate predicate, int atLine) throws ProducerException;
    void hasInterface(ImplementsPredicate predicate) throws ProducerException;
}
