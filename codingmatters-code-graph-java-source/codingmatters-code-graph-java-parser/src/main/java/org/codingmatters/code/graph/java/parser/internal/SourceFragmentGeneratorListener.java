package org.codingmatters.code.graph.java.parser.internal;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.codingmatters.code.graph.java.ast.JavaBaseListener;
import org.codingmatters.code.graph.java.ast.JavaParser;
import org.codingmatters.code.graph.java.parser.fragments.*;

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
    private final TypeSupport typeSupport;
    private final ParsingSupport support;
    
    private final NamingContext namingContext = new NamingContext();
    private final InnerClassCounter innerClassCounter = new InnerClassCounter();
    
    private boolean inClass = false;


    public SourceFragmentGeneratorListener(FragmentStream stream, ClassDisambiguizer disambiguizer) {
        this.stream = stream;
        this.typeSupport = new TypeSupport(this.namingContext, disambiguizer);
        this.support = new ParsingSupport(this.typeSupport);
    }

    @Override
    public void enterCompilationUnit(@NotNull JavaParser.CompilationUnitContext ctx) {
        if(ctx.packageDeclaration() != null) {
            JavaParser.QualifiedNameContext qualifiedName = ctx.packageDeclaration().qualifiedName();
            Token firstSymbol = qualifiedName.Identifier().get(0).getSymbol();
            Token lastSymbol = qualifiedName.Identifier().get(qualifiedName.Identifier().size() - 1).getSymbol();

            try {
                String cannonicalName = this.support.canonical(qualifiedName.getText());
                this.stream.fragment(fragmentBuilder()
                        .withText(qualifiedName.getText())
                        .withQualifiedName(cannonicalName)
                        .withStart(firstSymbol.getStartIndex())
                        .withEnd(lastSymbol.getStopIndex())
                        .build(PackageFragment.class));
            } catch (AbstractFragment.Builder.BuilderException e) {
                throw new SourceFragmentUncheckedException("error parsing compilation unit", e);
            }

            this.namingContext.next(qualifiedName.getText());
        } else {
            this.namingContext.next("");
        }
    }


    @Override
    public void exitCompilationUnit(@NotNull JavaParser.CompilationUnitContext ctx) {
        this.namingContext.previous();
    }
 
    @Override
    public void enterImportDeclaration(@NotNull JavaParser.ImportDeclarationContext ctx) {
        JavaParser.QualifiedNameContext qualifiedName = ctx.qualifiedName();
        Token firstSymbol = qualifiedName.Identifier().get(0).getSymbol();
        Token lastSymbol = qualifiedName.Identifier().get(qualifiedName.Identifier().size() - 1).getSymbol();

        this.support.addImport(qualifiedName.getText());

        try {
            String cannonicalName = this.support.canonical(qualifiedName.getText());
            this.stream.fragment(fragmentBuilder()
                    .withText(qualifiedName.getText())
                    .withQualifiedName(cannonicalName)
                    .withStart(firstSymbol.getStartIndex())
                    .withEnd(lastSymbol.getStopIndex())
                    .build(ImportFragment.class));
        } catch (AbstractFragment.Builder.BuilderException e) {
            throw new SourceFragmentUncheckedException("error parsing import declaration", e);
        }
    }
    
    @Override
    public void enterClassDeclaration(@NotNull JavaParser.ClassDeclarationContext ctx) {
        String name = ctx.Identifier().getText();
        String qualifiedName;
        if(this.inClass) {
            qualifiedName = this.namingContext.current() + "$" + name;
        } else if(this.namingContext.current().isEmpty()) {
            qualifiedName = name;
        } else {
            qualifiedName = this.namingContext.current() + "." + name;
        }
        String canonicalName = this.support.canonical(qualifiedName);
        
        Token symbol = ctx.Identifier().getSymbol();

        try {
            this.stream.fragment(fragmentBuilder()
                    .withText(ctx.Identifier().getText())
                    .withQualifiedName(canonicalName)
                    .withStart(symbol.getStartIndex())
                    .withEnd(symbol.getStopIndex())
                    .build(ClassDeclarationFragment.class));
        } catch (AbstractFragment.Builder.BuilderException e) {
            throw new SourceFragmentUncheckedException("error parsing class declaration", e);
        }

        this.namingContext.next(qualifiedName);
        this.innerClassCounter.next();
        this.inClass = true;
    }

    @Override
    public void exitClassDeclaration(@NotNull JavaParser.ClassDeclarationContext ctx) {
        this.inClass = false;
        this.innerClassCounter.previous();
        this.namingContext.previous();
    }

    @Override
    public void enterFieldDeclaration(@NotNull JavaParser.FieldDeclarationContext ctx) {
        try {
            this.emmitClassUsageForType(ctx.type());
            for (JavaParser.VariableDeclaratorContext variableDeclaratorContext : ctx.variableDeclarators().variableDeclarator()) {
                this.emmitFieldDeclarationForVariableDeclarator(variableDeclaratorContext);
            }
        } catch (AbstractFragment.Builder.BuilderException | DisambiguizerException e) {
            throw new SourceFragmentUncheckedException("error parsing field declaration", e);
        }
    }
    
    private void emmitClassUsageForType(JavaParser.TypeContext type) throws DisambiguizerException, AbstractFragment.Builder.BuilderException {
        String name = type.getText();
        String packageName = this.support.choosePackageForTypeName(name);
        this.stream.fragment(fragmentBuilder()
                .withQualifiedName(this.support.canonical(packageName + "." + name))
                .withText(name)
                .withStart(type.getStart().getStartIndex())
                .withEnd(type.getStart().getStopIndex())
                .build(ClassUsageFragment.class));
    }

    private void emmitFieldDeclarationForVariableDeclarator(JavaParser.VariableDeclaratorContext ctx) throws AbstractFragment.Builder.BuilderException {
        TerminalNode identifier = ctx.variableDeclaratorId().Identifier();
        this.stream.fragment(fragmentBuilder()
                .withQualifiedName(this.support.canonical(this.namingContext.current()) + "#" + identifier.getText())
                .withText(identifier.getText())
                .withStart(identifier.getSymbol().getStartIndex())
                .withEnd(identifier.getSymbol().getStopIndex())
                .build(FieldDeclarationFragment.class));
    }

    @Override
    public void enterMethodDeclaration(@NotNull JavaParser.MethodDeclarationContext ctx) {
        try {
            this.stream.fragment(fragmentBuilder()
                            .withQualifiedName(this.support.canonical(this.namingContext.current()) + "#" + this.support.methodLocalName(ctx))
                            .withText(ctx.Identifier().getText())
                            .withStart(ctx.Identifier().getSymbol().getStartIndex())
                            .withEnd(ctx.Identifier().getSymbol().getStopIndex())
                            .build(MethodDeclarationFragment.class)
            );
        } catch (AbstractFragment.Builder.BuilderException | DisambiguizerException e) {
            throw new SourceFragmentUncheckedException("error parsing method declaration", e);
        }
    }
    
    @Override
    public void enterConstructorDeclaration(@NotNull JavaParser.ConstructorDeclarationContext ctx) {
        try {
            this.stream.fragment(fragmentBuilder()
                            .withQualifiedName(this.support.canonical(this.namingContext.current()) + "#" + this.support.constructorLocalName(ctx))
                            .withText(ctx.Identifier().getText())
                            .withStart(ctx.Identifier().getSymbol().getStartIndex())
                            .withEnd(ctx.Identifier().getSymbol().getStopIndex())
                            .build(MethodDeclarationFragment.class)
            );
        } catch (AbstractFragment.Builder.BuilderException | DisambiguizerException e) {
            throw new SourceFragmentUncheckedException("error parsing constructor declaration", e);
        }
    }

    @Override
    public void enterCreator(@NotNull JavaParser.CreatorContext ctx) {
        if(this.support.isAnonymousClassDeclaration(ctx)) {
            this.namingContext.next(this.namingContext.current() + "$" + this.innerClassCounter.currentNextValue());

            this.innerClassCounter.next();
        }
    }

    @Override
    public void exitCreator(@NotNull JavaParser.CreatorContext ctx) {
        if(this.support.isAnonymousClassDeclaration(ctx)) {
            this.innerClassCounter.previous();
            this.namingContext.previous();
        }
    }
}
