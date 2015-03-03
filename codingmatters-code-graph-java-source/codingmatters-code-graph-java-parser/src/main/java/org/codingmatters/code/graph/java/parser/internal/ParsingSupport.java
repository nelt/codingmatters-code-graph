package org.codingmatters.code.graph.java.parser.internal;

import org.antlr.v4.runtime.misc.NotNull;
import org.codingmatters.code.graph.java.ast.JavaParser;

import java.util.ArrayList;
import java.util.LinkedList;

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
        if(type != null) {
            return this.qualifiedTypeSpec(type.getText());
        } else {
            return "V";
        }
    }
    
    public String canonical(String text) {
        return text.replaceAll("\\.", "/");
    }

    private String formalParametersTypesSpec(JavaParser.FormalParametersContext formalParameters) throws DisambiguizerException {
        StringBuilder result = new StringBuilder("");
        if(formalParameters.formalParameterList() != null) {
            for (JavaParser.FormalParameterContext formalParameterContext : formalParameters.formalParameterList().formalParameter()) {
                String typeName = formalParameterContext.type().getText();
                result.append(this.qualifiedTypeSpec(typeName));
            }
            if(formalParameters.formalParameterList().lastFormalParameter() != null) {
                String typeName = formalParameters.formalParameterList().lastFormalParameter().type().getText();
                result.append(this.qualifiedTypeSpec(typeName + "[]"));
            }
        }
        return result.toString();
    }
    
    private String qualifiedTypeSpec(String simpleName) throws DisambiguizerException {
        String arrayMark = "";
        if(simpleName.endsWith("[]")) {
            simpleName = simpleName.substring(0, simpleName.length() - "[]".length());
            arrayMark = "[";
        }
        
        if(this.isPrimitive(simpleName)) {
            return String.format("%s%s",
                    arrayMark,
                    this.primitiveTypeMarker(simpleName)
            );
        } else {
            String packageName = this.choosePackageForTypeName(simpleName);
            if (packageName != null && !packageName.isEmpty()) {
                packageName = packageName.replace('.', '/') + "/";
            }
            return String.format("%sL%s%s;",
                    arrayMark,
                    packageName,
                    simpleName
            );
        }
    }

    private String primitiveTypeMarker(String simpleName) {
        if(simpleName.equals("int")) {
            return "I";
        } else {
            throw new SourceFragmentUncheckedException("unsupported primitive type : " + simpleName);
        }
    }

    private boolean isPrimitive(String simpleName) {
        return simpleName.equals("int");
    }


    private String[] builCandidates() {
        ArrayList<String> candidates = new ArrayList<>();
        candidates.add("java.lang");
        candidates.addAll(this.imports);
        if(! this.namingContext.current().isEmpty()) {
            candidates.add(this.namingContext.current());
        }
        return candidates.toArray(new String[candidates.size()]);
    }

}
