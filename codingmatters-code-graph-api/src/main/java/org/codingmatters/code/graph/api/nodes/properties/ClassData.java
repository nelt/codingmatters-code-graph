package org.codingmatters.code.graph.api.nodes.properties;

import org.codingmatters.code.graph.api.nodes.properties.annotations.StorableProperties;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 12/07/14
 * Time: 03:41
 * To change this template use File | Settings | File Templates.
 */
public class ClassData {
    
    private ClassInformationProperties information;
    private SourceLocationProperties declaration;
    
    @StorableProperties
    public ClassInformationProperties getInformation() {
        return information;
    }

    public ClassData withInformation(ClassInformationProperties.Builder information) {
        this.information = information.build();
        return this;
    }

    @StorableProperties
    public SourceLocationProperties getDeclaration() {
        return declaration;
    }

    public ClassData withDeclaration(SourceLocationProperties.Builder declaration) {
        this.declaration = declaration.build();
        return this;
    }

}
