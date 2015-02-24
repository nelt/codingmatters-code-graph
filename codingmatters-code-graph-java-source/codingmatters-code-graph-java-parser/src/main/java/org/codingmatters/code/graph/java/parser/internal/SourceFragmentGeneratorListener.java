package org.codingmatters.code.graph.java.parser.internal;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.codingmatters.code.graph.java.ast.JavaBaseListener;
import org.codingmatters.code.graph.java.ast.JavaParser;
import org.codingmatters.code.graph.java.parser.fragments.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
* Created with IntelliJ IDEA.
* User: nel
* Date: 23/07/14
* Time: 07:20
* To change this template use File | Settings | File Templates.
*/
public class SourceFragmentGeneratorListener extends JavaBaseListener {


    private static AbstractFragment.Builder fragmentBuilder() {
        return new AbstractFragment.Builder();
    }
    
    private final FragmentStream stream;
    private final ClassDisambiguizer disambiguizer;

    private final Stack<String> namingStack = new Stack<>();
    private boolean inClass = false;

    private final LinkedList<String> imports = new LinkedList<>();

    private Stack<AtomicInteger> innerClassCounters = new Stack<>();

    public SourceFragmentGeneratorListener(FragmentStream stream, ClassDisambiguizer disambiguizer) {
        this.stream = stream;
        this.disambiguizer = disambiguizer;
    }

    @Override
    public void enterCompilationUnit(@NotNull JavaParser.CompilationUnitContext ctx) {
        JavaParser.QualifiedNameContext qualifiedName = ctx.packageDeclaration().qualifiedName();
        Token firstSymbol = qualifiedName.Identifier().get(0).getSymbol();
        Token lastSymbol = qualifiedName.Identifier().get(qualifiedName.Identifier().size() - 1).getSymbol();
        
        try {
            this.stream.fragment(fragmentBuilder()
                    .withText(qualifiedName.getText())
                    .withQualifiedName(qualifiedName.getText())
                    .withStart(firstSymbol.getStartIndex())
                    .withEnd(lastSymbol.getStopIndex())
                    .build(PackageFragment.class));
        } catch (AbstractFragment.Builder.BuilderException e) {
            e.printStackTrace();
        }

        this.namingStack.push(qualifiedName.getText());
        
        super.enterCompilationUnit(ctx);
    }

    @Override
    public void enterImportDeclaration(@NotNull JavaParser.ImportDeclarationContext ctx) {
        JavaParser.QualifiedNameContext qualifiedName = ctx.qualifiedName();
        Token firstSymbol = qualifiedName.Identifier().get(0).getSymbol();
        Token lastSymbol = qualifiedName.Identifier().get(qualifiedName.Identifier().size() - 1).getSymbol();

        this.imports.add(qualifiedName.getText());

        try {
            this.stream.fragment(fragmentBuilder()
                    .withText(qualifiedName.getText())
                    .withQualifiedName(qualifiedName.getText())
                    .withStart(firstSymbol.getStartIndex())
                    .withEnd(lastSymbol.getStopIndex())
                    .build(ImportFragment.class));
        } catch (AbstractFragment.Builder.BuilderException e) {
            e.printStackTrace();
        }
        
        super.enterImportDeclaration(ctx);
    }

    @Override
    public void exitCompilationUnit(@NotNull JavaParser.CompilationUnitContext ctx) {
        super.exitCompilationUnit(ctx);
        this.namingStack.pop();
    }

    @Override
    public void enterClassDeclaration(@NotNull JavaParser.ClassDeclarationContext ctx) {
        String name = ctx.Identifier().getText();
        String qualifiedName = this.namingStack.peek() + (this.inClass ? "$" : ".") + name;
        Token symbol = ctx.Identifier().getSymbol();

        try {
            this.stream.fragment(fragmentBuilder()
                    .withText(ctx.Identifier().getText())
                    .withQualifiedName(qualifiedName)
                    .withStart(symbol.getStartIndex())
                    .withEnd(symbol.getStopIndex())
                    .build(ClassDeclarationFragment.class));
        } catch (AbstractFragment.Builder.BuilderException e) {
            e.printStackTrace();
        }
        
        this.namingStack.push(qualifiedName);
        this.addInnerClassCounterLevel();
        
        this.inClass = true;
        
        super.enterClassDeclaration(ctx);
    }

    @Override
    public void exitClassDeclaration(@NotNull JavaParser.ClassDeclarationContext ctx) {
        this.inClass = false;

        this.popInnerClassCounterLevel();
        this.namingStack.pop();
        
        super.exitClassDeclaration(ctx);
    }

    @Override
    public void enterFieldDeclaration(@NotNull JavaParser.FieldDeclarationContext ctx) {
        super.enterFieldDeclaration(ctx);
        JavaParser.TypeContext type = ctx.type();

        this.emmitClassUsageForType(type);

        for (JavaParser.VariableDeclaratorContext variableDeclaratorContext : ctx.variableDeclarators().variableDeclarator()) {
            this.emmitFieldDeclarationForVariableDeclarator(variableDeclaratorContext);
        }
    }

    @Override
    public void enterMethodDeclaration(@NotNull JavaParser.MethodDeclarationContext ctx) {
        super.enterMethodDeclaration(ctx);

        try {
            this.stream.fragment(fragmentBuilder()
                    .withQualifiedName(this.namingStack.peek() + "#" + this.methodLocalName(ctx))
                    .withText(ctx.Identifier().getText())
                    .withStart(ctx.Identifier().getSymbol().getStartIndex())
                    .withEnd(ctx.Identifier().getSymbol().getStopIndex())
                    .build(MethodDeclarationFragment.class)
            );
        } catch (AbstractFragment.Builder.BuilderException e) {
            e.printStackTrace();
        } catch (DisambiguizerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void enterConstructorDeclaration(@NotNull JavaParser.ConstructorDeclarationContext ctx) {
        super.enterConstructorDeclaration(ctx);
        
        try {
            this.stream.fragment(fragmentBuilder()
                            .withQualifiedName(this.namingStack.peek() + "#" + this.constructorLocalName(ctx))
                            .withText(ctx.Identifier().getText())
                            .withStart(ctx.Identifier().getSymbol().getStartIndex())
                            .withEnd(ctx.Identifier().getSymbol().getStopIndex())
                            .build(MethodDeclarationFragment.class)
            );
        } catch (AbstractFragment.Builder.BuilderException e) {
            e.printStackTrace();
        } catch (DisambiguizerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void enterCreator(@NotNull JavaParser.CreatorContext ctx) {
        super.enterCreator(ctx);
        if(this.isAnonymousClassDeclaration(ctx)) {
            int innerCount = this.innerClassCounters.peek().incrementAndGet();
            this.namingStack.push(this.namingStack.peek() + "$" + innerCount);

            this.addInnerClassCounterLevel();
        }
    }

    @Override
    public void exitCreator(@NotNull JavaParser.CreatorContext ctx) {
        if(this.isAnonymousClassDeclaration(ctx)) {
            this.popInnerClassCounterLevel();
            this.namingStack.pop();
        }
        super.exitCreator(ctx);
    }

    private boolean isAnonymousClassDeclaration(@NotNull JavaParser.CreatorContext ctx) {
        return ctx.classCreatorRest() != null && ctx.classCreatorRest().classBody() != null;
    }
    

    private void addInnerClassCounterLevel() {
        this.innerClassCounters.push(new AtomicInteger(0));
    }

    private void popInnerClassCounterLevel() {
        this.innerClassCounters.pop();
    }

    private void emmitClassUsageForType(JavaParser.TypeContext type) {
        String name = type.getText();
        try {
            String packageName = this.choosePackageForTypeName(name);
            this.stream.fragment(fragmentBuilder()
                    .withQualifiedName(packageName + "." + name)
                    .withText(name)
                    .withStart(type.getStart().getStartIndex())
                    .withEnd(type.getStart().getStopIndex())
                    .build(ClassUsageFragment.class));
        } catch (DisambiguizerException e) {
            e.printStackTrace();
        } catch (AbstractFragment.Builder.BuilderException e) {
            e.printStackTrace();
        }
    }

    private String choosePackageForTypeName(String name) throws DisambiguizerException {
        return this.disambiguizer.choosePackage(name, this.builCandidates());
    }


    private String[] builCandidates() {
        ArrayList<String> candidates = new ArrayList<>();
        candidates.add("java.lang");
        candidates.addAll(this.imports);
        candidates.add(this.namingStack.peek());

        return candidates.toArray(new String[candidates.size()]);
    }

    private void emmitFieldDeclarationForVariableDeclarator(JavaParser.VariableDeclaratorContext ctx) {
        TerminalNode identifier = ctx.variableDeclaratorId().Identifier();
        try {
            this.stream.fragment(fragmentBuilder()
                    .withQualifiedName(this.namingStack.peek() + "#" + identifier.getText())
                    .withText(identifier.getText())
                    .withStart(identifier.getSymbol().getStartIndex())
                    .withEnd(identifier.getSymbol().getStopIndex())
                    .build(FieldDeclarationFragment.class));
        } catch (AbstractFragment.Builder.BuilderException e) {
            e.printStackTrace();
        }
    }

    
    private String constructorLocalName(JavaParser.ConstructorDeclarationContext ctx) throws DisambiguizerException {
        return String.format("<init>(%s)V",
                this.formalParametersTypesSpec(ctx.formalParameters()));
    }
    
    private String methodLocalName(JavaParser.MethodDeclarationContext ctx) throws DisambiguizerException {
        return String.format("%s(%s)%s",
                ctx.Identifier().getText(),
                this.formalParametersTypesSpec(ctx.formalParameters()),
                this.typeSpec(ctx.type()));
    }

    private String typeSpec(JavaParser.TypeContext type) throws DisambiguizerException {
        String returnType = "";
        if(type != null) {
            String typeName = type.getText();
            returnType = this.typeInMethodIdentifier(typeName);
        }
        return returnType;
    }

    private String typeInMethodIdentifier(String simpleName) throws DisambiguizerException {
        return String.format("L%s/%s;", 
                this.choosePackageForTypeName(simpleName).replace('.', '/'), 
                simpleName);
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
}
