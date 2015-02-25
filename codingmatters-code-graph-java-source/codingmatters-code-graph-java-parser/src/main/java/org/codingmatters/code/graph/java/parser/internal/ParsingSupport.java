package org.codingmatters.code.graph.java.parser.internal;

import org.antlr.v4.runtime.misc.NotNull;
import org.codingmatters.code.graph.java.ast.JavaParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by nel on 25/02/15.
 */
public class ParsingSupport {
    
    private final ClassDisambiguizer disambiguizer;
    private final NamingContext namingContext;
            
    private final LinkedList<String> imports = new LinkedList<>();

    public ParsingSupport(ClassDisambiguizer disambiguizer, NamingContext namingContext) {
        this.disambiguizer = disambiguizer;
        this.namingContext = namingContext;
    }

    public boolean isAnonymousClassDeclaration(@NotNull JavaParser.CreatorContext ctx) {
        return ctx.classCreatorRest() != null && ctx.classCreatorRest().classBody() != null;
    }

    public String constructorLocalName(JavaParser.ConstructorDeclarationContext ctx) throws DisambiguizerException {
        return String.format("<init>(%s)V",
                this.formalParametersTypesSpec(ctx.formalParameters()));
    }

    public String methodLocalName(JavaParser.MethodDeclarationContext ctx) throws DisambiguizerException {
        return String.format("%s(%s)%s",
                ctx.Identifier().getText(),
                this.formalParametersTypesSpec(ctx.formalParameters()),
                this.typeSpec(ctx.type()));
    }

    public String choosePackageForTypeName(String name) throws DisambiguizerException {
        return this.disambiguizer.choosePackage(name, this.builCandidates());
    }

    public void addImport(String qualifiedNameText) {
        this.imports.add(qualifiedNameText);
    }
    
    
    
    
    private String typeSpec(JavaParser.TypeContext type) throws DisambiguizerException {
        String returnType = "";
        if(type != null) {
            String typeName = type.getText();
            returnType = this.typeInMethodIdentifier(typeName);
        }
        return returnType;
    }


    private String formalParametersTypesSpec(JavaParser.FormalParametersContext formalParameters) throws DisambiguizerException {
        StringBuilder result = new StringBuilder("");
        if(formalParameters.formalParameterList() != null) {
            for (JavaParser.FormalParameterContext formalParameterContext : formalParameters.formalParameterList().formalParameter()) {
                String typeName = formalParameterContext.type().getText();
                result.append(this.typeInMethodIdentifier(typeName));
            }
            if(formalParameters.formalParameterList().lastFormalParameter() != null) {
                String typeName = formalParameters.formalParameterList().lastFormalParameter().type().getText();
                result.append(this.typeInMethodIdentifier(typeName));
            }
        }
        return result.toString();
    }
    
    private String typeInMethodIdentifier(String simpleName) throws DisambiguizerException {
        return String.format("L%s/%s;",
                this.choosePackageForTypeName(simpleName).replace('.', '/'),
                simpleName);
    }



    private String[] builCandidates() {
        ArrayList<String> candidates = new ArrayList<>();
        candidates.add("java.lang");
        candidates.addAll(this.imports);
        candidates.add(this.namingContext.current());

        return candidates.toArray(new String[candidates.size()]);
    }

}
