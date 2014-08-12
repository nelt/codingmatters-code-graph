package org.codingmatters.code.graph.java.parser.internal;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.codingmatters.code.graph.java.ast.JavaBaseListener;
import org.codingmatters.code.graph.java.ast.JavaParser;
import org.codingmatters.code.graph.java.parser.fragments.*;

import java.util.ArrayList;
import java.util.LinkedList;
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
    private final ClassDisambiguizer disambiguizer;

    private final Stack<String> namingStack = new Stack<>();
    private boolean inClass = false;

    private final LinkedList<String> imports = new LinkedList<>();

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

        this.imports.add(qualifiedName.getText());

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

    @Override
    public void enterFieldDeclaration(@NotNull JavaParser.FieldDeclarationContext ctx) {
        super.enterFieldDeclaration(ctx);

        String name = ctx.type().getText();
        String qualifiedName = null;
        System.out.println(name);


        ArrayList<String> candidates = new ArrayList<>();
        candidates.add("java.lang");
        candidates.addAll(this.imports);
        candidates.add(this.namingStack.peek());

        try {
            String packageName = this.disambiguizer.choosePackage(name, candidates.toArray(new String[candidates.size()]));
            this.stream.fragment(new AbstractFragment.Builder()
                    .withQualifiedName(packageName + "." + name)
                    .withText(name)
                    .withStart(ctx.type().getStart().getStartIndex())
                    .withEnd(ctx.type().getStart().getStopIndex())
                    .build(ClassUsageFragment.class));
        } catch (DisambiguizerException e) {
            e.printStackTrace();
        } catch (AbstractFragment.Builder.BuilderException e) {
            e.printStackTrace();
        }

        if(this.exists("java.lang." + name)) {
            qualifiedName = "java.lang." + name;
        } else {
            for (String anImport : this.imports) {
                if(anImport.endsWith("." + name)) {
                    qualifiedName = anImport + "." + name;
                    break;
                }
            }
        }
        System.out.println(qualifiedName);



    }

    private boolean exists(String qualifiedName) {
        boolean isLang = true;
        try {
            Class.forName(qualifiedName);
        } catch (ClassNotFoundException e) {
            isLang = false;
        }
        return isLang;
    }
}
