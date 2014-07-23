package org.codingmatters.code.graph.java.parser.internal;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.codingmatters.code.graph.java.ast.JavaBaseListener;
import org.codingmatters.code.graph.java.ast.JavaParser;
import org.codingmatters.code.graph.java.parser.fragments.*;

import java.util.Stack;

/**
* Created with IntelliJ IDEA.
* User: nel
* Date: 23/07/14
* Time: 07:20
* To change this template use File | Settings | File Templates.
*/
public class SourceFragmentGeneratorListener extends JavaBaseListener {
    
    private final FragmentStream stream;
    
    private final Stack<String> namingStack = new Stack<>();
    private boolean inClass = false;

    public SourceFragmentGeneratorListener(FragmentStream stream) {
        this.stream = stream;
    }

    @Override
    public void enterCompilationUnit(@NotNull JavaParser.CompilationUnitContext ctx) {
        JavaParser.QualifiedNameContext qualifiedName = ctx.packageDeclaration().qualifiedName();
        Token firstSymbol = qualifiedName.Identifier().get(0).getSymbol();
        Token lastSymbol = qualifiedName.Identifier().get(qualifiedName.Identifier().size() - 1).getSymbol();
        
        try {
            this.stream.fragment(new AbstractFragment.Builder()
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

        try {
            this.stream.fragment(new AbstractFragment.Builder()
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
        System.out.println(String.format("CLASS %s (%s) [%s, %s]",
                ctx.Identifier().getText(),
                this.namingStack.peek(),
                ctx.Identifier().getSymbol().getLine(),
                ctx.Identifier().getSymbol().getCharPositionInLine()
        ));

        String name = ctx.Identifier().getText();
        String qualifiedName = this.namingStack.peek() + (this.inClass ? "$" : ".") + name;
        Token symbol = ctx.Identifier().getSymbol();

        try {
            
            this.stream.fragment(new AbstractFragment.Builder()
                    .withText(ctx.Identifier().getText())
                    .withQualifiedName(qualifiedName)
                    .withStart(symbol.getStartIndex())
                    .withEnd(symbol.getStopIndex())
                    .build(ClassDeclarationFragment.class));
        } catch (AbstractFragment.Builder.BuilderException e) {
            e.printStackTrace();
        }
        
        this.namingStack.push(qualifiedName);
        this.inClass = true;
        super.enterClassDeclaration(ctx);
    }

    @Override
    public void exitClassDeclaration(@NotNull JavaParser.ClassDeclarationContext ctx) {
        super.exitClassDeclaration(ctx);
        this.inClass = false;
        this.namingStack.pop();
    }
}
