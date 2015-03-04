package org.codingmatters.code.graph.java.parser.internal;

import org.codingmatters.code.graph.java.ast.JavaParser;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by nel on 03/03/15.
 */
public class TypeSupport {

    private final NamingContext namingContext;
    private final ClassDisambiguizer disambiguizer;

    private final LinkedList<String> imports = new LinkedList<>();

    public TypeSupport(NamingContext namingContext, ClassDisambiguizer disambiguizer) {
        this.namingContext = namingContext;
        this.disambiguizer = disambiguizer;
    }

    public String typeSpec(JavaParser.TypeContext type) throws DisambiguizerException {
        if(type != null) {
            return this.typeSpec(type.getText());
        } else {
            return "V";
        }
    }

    public String typeSpec(String simpleName) throws DisambiguizerException {
        String arrayMark = "";
        if(simpleName.endsWith("[]")) {
            simpleName = simpleName.substring(0, simpleName.length() - "[]".length());
            arrayMark = "[";
        }

        if(PrimitiveType.isPrimitive(simpleName)) {
            return String.format("%s%s",
                    arrayMark,
                    PrimitiveType.forType(simpleName).getSpec()
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


    public String qualifiedType(String typeName) throws DisambiguizerException {
        String packageName = this.choosePackageForTypeName(typeName);
        return packageName + "." + typeName;
    }
    
    public String formalParametersTypesSpec(JavaParser.FormalParametersContext formalParameters) throws DisambiguizerException {
        StringBuilder result = new StringBuilder("");
        if(formalParameters.formalParameterList() != null) {
            for (JavaParser.FormalParameterContext formalParameterContext : formalParameters.formalParameterList().formalParameter()) {
                String typeName = formalParameterContext.type().getText();
                result.append(this.typeSpec(typeName));
            }
            if(formalParameters.formalParameterList().lastFormalParameter() != null) {
                String typeName = formalParameters.formalParameterList().lastFormalParameter().type().getText();
                result.append(this.typeSpec(typeName + "[]"));
            }
        }
        return result.toString();
    }


    
    private String choosePackageForTypeName(String name) throws DisambiguizerException {
        return this.disambiguizer.choosePackage(name, this.builCandidates());
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

    public void addImport(String qualifiedNameText) {
        this.imports.add(qualifiedNameText);
    }

}
