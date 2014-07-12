package org.codingmatters.code.graph.api.nodes.properties;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 12/07/14
 * Time: 03:41
 * To change this template use File | Settings | File Templates.
 */
public class ClassProperties {
    private ClassInformation information;
    private SourceLocation declaration;

    public ClassInformation getInformation() {
        return information;
    }

    public ClassProperties withInformation(ClassInformation.Builder information) {
        this.information = information.build();
        return this;
    }

    public SourceLocation getDeclaration() {
        return declaration;
    }

    public ClassProperties withDeclaration(SourceLocation.Builder declaration) {
        this.declaration = declaration.build();
        return this;
    }
}
