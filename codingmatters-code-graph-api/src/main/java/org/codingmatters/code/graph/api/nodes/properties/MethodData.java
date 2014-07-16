package org.codingmatters.code.graph.api.nodes.properties;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 16/07/14
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class MethodData {
    
    private MethodSignatureProperties signature;

    public MethodSignatureProperties getSignature() {
        return signature;
    }

    public MethodData withSignature(MethodSignatureProperties.Builder signatureBuilder) {
        this.signature = signatureBuilder.build();
        return this;
    }
}
