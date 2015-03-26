package org.codingmatters.code.graph.java.parser.ast;

import org.codingmatters.code.graph.java.ast.JavaParser;

import java.util.HashMap;

/**
 * Created by nel on 26/03/15.
 */
public class Scope {
    
    private final Scope parent;
    private final HashMap<String, String> variableTypeSpecs = new HashMap<>();

    public Scope() {
        this(null);
    }
    
    public Scope(Scope parent) {
        this.parent = parent;
    }

    public String typeSpec(String variable) {
        if(this.variableTypeSpecs.containsKey(variable)) {
            return this.variableTypeSpecs.get(variable);
        } else if(this.parent != null) {
            return this.parent.typeSpec(variable);
        } else {
            return null;
        }
    }
    
    public boolean isTypeSpecDefined(String variable) {
        if(this.variableTypeSpecs.containsKey(variable)) {
            return true;
        } else if(this.parent != null) {
            return this.parent.isTypeSpecDefined(variable);
        } else {
            return false;
        }
    }
    
    public void define(String variable, String typeSpec) {
        this.variableTypeSpecs.put(variable, typeSpec);
    }
    
    public void define(String typeSpec, JavaParser.VariableDeclaratorsContext variableDeclaratorsContext) throws DisambiguizerException {
        for (JavaParser.VariableDeclaratorContext declaratorCtx : variableDeclaratorsContext.variableDeclarator()) {
            String identifier = declaratorCtx.variableDeclaratorId().Identifier().getText();
            this.define(identifier, typeSpec);
        }
    }
    
    public Scope child() {
        return new Scope(this);
    }
    public Scope child(String thisTypeSpec) {
        Scope result = new Scope(this);
        result.define("this", thisTypeSpec);
        return result;
    }
    
    public Scope parent() {
        return this.parent;
    }

    public String resolveType(JavaParser.ExpressionContext expression) {
        
        return this.typeSpec(expression.getText());
    }

    @Override
    public String toString() {
        return "Scope{" +
                "parent=" + parent +
                ", variableTypeSpecs=" + variableTypeSpecs +
                '}';
    }
}
