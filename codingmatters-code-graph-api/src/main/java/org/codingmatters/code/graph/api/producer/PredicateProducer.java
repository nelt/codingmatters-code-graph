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
    public void hasParent(ExtendsPredicate predicate) throws ProducerException;
    public void hasField(HasFieldPredicate predicate) throws ProducerException;
    public void hasMethod(HasMethodPredicate predicate) throws ProducerException;
    public void hasInner(HasInnerClassPredicate predicate) throws ProducerException;
    public void usage(UsesPredicate predicate) throws ProducerException;
}
