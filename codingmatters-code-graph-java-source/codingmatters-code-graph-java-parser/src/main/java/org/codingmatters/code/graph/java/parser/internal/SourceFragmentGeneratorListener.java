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
    private final ParsingSupport support;
    
    private final NamingContext namingContext = new NamingContext();
    private final InnerClassCounter innerClassCounter = new InnerClassCounter();
    
    private boolean inClass = false;


    public SourceFragmentGeneratorListener(FragmentStream stream, ClassDisambiguizer disambiguizer) {
        this.stream = stream;
        this.support = new ParsingSupport(disambiguizer, this.namingContext);
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

        this.namingContext.next(qualifiedName.getText());
        
        super.enterCompilationUnit(ctx);
    }
 
    @Override
    public void enterImportDeclaration(@NotNull JavaParser.ImportDeclarationContext ctx) {
        JavaParser.QualifiedNameContext qualifiedName = ctx.qualifiedName();
        Token firstSymbol = qualifiedName.Identifier().get(0).getSymbol();
        Token lastSymbol = qualifiedName.Identifier().get(qualifiedName.Identifier().size() - 1).getSymbol();

        this.support.addImport(qualifiedName.getText());

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
        this.namingContext.previous();
    }
    

    @Override
    public void enterClassDeclaration(@NotNull JavaParser.ClassDeclarationContext ctx) {
        String name = ctx.Identifier().getText();
        String qualifiedName = this.namingContext.current() + (this.inClass ? "$" : ".") + name;
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

        this.namingContext.next(qualifiedName);
        this.innerClassCounter.next();
        
        this.inClass = true;
        
        super.enterClassDeclaration(ctx);
    }

    @Override
    public void exitClassDeclaration(@NotNull JavaParser.ClassDeclarationContext ctx) {
        this.inClass = false;

        this.innerClassCounter.previous();
        this.namingContext.previous();
        
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
    
    private void emmitClassUsageForType(JavaParser.TypeContext type) {
        String name = type.getText();
        try {
            String packageName = this.support.choosePackageForTypeName(name);
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

    private void emmitFieldDeclarationForVariableDeclarator(JavaParser.VariableDeclaratorContext ctx) {
        TerminalNode identifier = ctx.variableDeclaratorId().Identifier();
        try {
            this.stream.fragment(fragmentBuilder()
                    .withQualifiedName(this.namingContext.current() + "#" + identifier.getText())
                    .withText(identifier.getText())
                    .withStart(identifier.getSymbol().getStartIndex())
                    .withEnd(identifier.getSymbol().getStopIndex())
                    .build(FieldDeclarationFragment.class));
        } catch (AbstractFragment.Builder.BuilderException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enterMethodDeclaration(@NotNull JavaParser.MethodDeclarationContext ctx) {
        super.enterMethodDeclaration(ctx);

        try {
            this.stream.fragment(fragmentBuilder()
                            .withQualifiedName(this.namingContext.current() + "#" + this.support.methodLocalName(ctx))
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
                            .withQualifiedName(this.namingContext.current() + "#" + this.support.constructorLocalName(ctx))
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
        super.exitCreator(ctx);
    }
    
}
