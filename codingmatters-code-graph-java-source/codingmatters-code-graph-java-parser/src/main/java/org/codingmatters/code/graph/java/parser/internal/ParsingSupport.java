package org.codingmatters.code.graph.java.parser.internal;

import org.antlr.v4.runtime.misc.NotNull;
import org.codingmatters.code.graph.java.ast.JavaParser;

/**
 * Created by nel on 25/02/15.
 */
public class ParsingSupport {
    
    private final TypeSupport typeSupport;

    public ParsingSupport(TypeSupport typeSupport) {
        this.typeSupport = typeSupport;
    }

    public boolean isAnonymousClassDeclaration(@NotNull JavaParser.CreatorContext ctx) {
        return ctx.classCreatorRest() != null && ctx.classCreatorRest().classBody() != null;
    }

    public String constructorLocalName(JavaParser.ConstructorDeclarationContext ctx) throws DisambiguizerException {
        return String.format("<init>(%s)V",
                this.typeSupport.formalParametersTypesSpec(ctx.formalParameters()));
    }

    public String methodLocalName(JavaParser.MethodDeclarationContext ctx) throws DisambiguizerException {
        return String.format("%s(%s)%s",
                ctx.Identifier().getText(),
                this.typeSupport.formalParametersTypesSpec(ctx.formalParameters()),
                this.typeSupport.typeSpec(ctx.type()));
    }

    public String choosePackageForTypeName(String name) throws DisambiguizerException {
        return this.typeSupport.choosePackageForTypeName(name);
    }

    public void addImport(String qualifiedNameText) {
        this.typeSupport.addImport(qualifiedNameText);
    }
    
    public String canonical(String text) {
        return text.replaceAll("\\.", "/");
    }
    
}
