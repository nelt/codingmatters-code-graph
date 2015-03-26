package org.codingmatters.code.graph.java.parser.ast;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.codingmatters.code.graph.java.ast.JavaBaseListener;
import org.codingmatters.code.graph.java.ast.JavaParser;
import org.codingmatters.code.graph.java.parser.ast.expression.MethodExpressionListener;
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
    
    private static AbstractFragment.Builder fragmentBuilder() {
        return new AbstractFragment.Builder();
    }
    
    private final FragmentStream stream;
    private final TypeSupport typeSupport;
    private final ParsingSupport support;
    
    private final NamingContext namingContext = new NamingContext();
    private final InnerClassCounter innerClassCounter = new InnerClassCounter();
    
    private boolean inClass = false;
    private final Stack<Declaration> currentDeclaration = new Stack<>();

    private Scope scope = new Scope();
    
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
            this.stream.fragment(fragmentBuilder().withText(qualifiedName.getText())
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
        String canonicalName = this.canonicalClassName(name);

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

        this.namingContext.next(this.qualifiedClassName(name));
        this.innerClassCounter.next();
        this.inClass = true;
        
        this.scope = this.scope.child(canonicalName);
    }

    private String qualifiedClassName(String name) {
        String qualifiedName;
        if(this.inClass) {
            qualifiedName = this.namingContext.current() + "$" + name;
        } else if(this.namingContext.current().isEmpty()) {
            qualifiedName = name;
        } else {
            qualifiedName = this.namingContext.current() + "." + name;
        }
        return qualifiedName;
    }


    private String canonicalClassName(String name) {
        return this.support.canonical(this.qualifiedClassName(name));
    }
    
    @Override
    public void exitClassDeclaration(@NotNull JavaParser.ClassDeclarationContext ctx) {
        this.inClass = false;
        this.innerClassCounter.previous();
        this.namingContext.previous();
        
        this.scope = this.scope.parent();
    }
    
    @Override
    public void enterFieldDeclaration(@NotNull JavaParser.FieldDeclarationContext ctx) {
        this.currentDeclaration.push(Declaration.FIELD);
    }
    @Override
    public void exitFieldDeclaration(@NotNull JavaParser.FieldDeclarationContext ctx) {
        try {
            this.scope.define(this.typeSupport.typeSpec(ctx.type()), ctx.variableDeclarators());
        } catch (DisambiguizerException e) {
            throw new SourceFragmentUncheckedException("error populating scope with field declaration", e);
        }
        this.currentDeclaration.pop();
    }

    

    @Override
    public void enterClassOrInterfaceType(@NotNull JavaParser.ClassOrInterfaceTypeContext ctx) {
        try {
            this.stream.fragment(fragmentBuilder()
                    .withQualifiedName(this.typeSupport.typeSpec(ctx.getText()))
                    .withText(ctx.getText())
                    .withStart(ctx.getStart().getStartIndex())
                    .withEnd(ctx.getStart().getStopIndex())
                    .build(ClassUsageFragment.class));
        } catch (AbstractFragment.Builder.BuilderException | DisambiguizerException e) {
            throw new SourceFragmentUncheckedException("error parsing field declaration", e);
        }
    }
    
    @Override
    public void enterVariableDeclarator(@NotNull JavaParser.VariableDeclaratorContext ctx) {
        if(this.currentDeclaration.isEmpty()) return;
        try {
            TerminalNode identifier = ctx.variableDeclaratorId().Identifier();
            this.stream.fragment(fragmentBuilder()
                    .withQualifiedName(this.support.canonical(this.namingContext.current()) + "#" + identifier.getText())
                    .withText(identifier.getText())
                    .withStart(identifier.getSymbol().getStartIndex())
                    .withEnd(identifier.getSymbol().getStopIndex())
                    .build(this.currentDeclaration.peek().variableFragment));
        } catch (AbstractFragment.Builder.BuilderException e) {
            throw new SourceFragmentUncheckedException("error parsing field declaration", e);
        }
    }

    @Override
    public void exitLocalVariableDeclaration(@NotNull JavaParser.LocalVariableDeclarationContext ctx) {
        try {
            this.scope.define(this.typeSupport.typeSpec(ctx.type()), ctx.variableDeclarators());
        } catch (DisambiguizerException e) {
            throw new SourceFragmentUncheckedException("error populating scope with local variables", e);
        }
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
        this.scope = this.scope.child();
    }
    
    @Override
    public void exitMethodDeclaration(@NotNull JavaParser.MethodDeclarationContext ctx) {
        this.scope = this.scope.parent();
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
        this.scope = this.scope.child();
    }

    @Override
    public void exitConstructorDeclaration(@NotNull JavaParser.ConstructorDeclarationContext ctx) {
        this.scope = this.scope.parent();
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

    @Override
    public void enterInterfaceDeclaration(@NotNull JavaParser.InterfaceDeclarationContext ctx) {
        this.scope = this.scope.child(this.canonicalClassName(ctx.Identifier().getText()));
    }

    @Override
    public void exitInterfaceDeclaration(@NotNull JavaParser.InterfaceDeclarationContext ctx) {
        this.scope = this.scope.parent();
    }

    @Override
    public void enterEnumDeclaration(@NotNull JavaParser.EnumDeclarationContext ctx) {
        this.scope = this.scope.child(this.canonicalClassName(ctx.Identifier().getText()));
    }

    @Override
    public void exitEnumDeclaration(@NotNull JavaParser.EnumDeclarationContext ctx) {
        this.scope = this.scope.parent();
    }

    @Override
    public void exitMethodCallExpression(@NotNull JavaParser.MethodCallExpressionContext ctx) {   
        System.out.println("method call : " + ctx.expression().getText() + " with " + (ctx.expressionList() != null ? ctx.expressionList().expression().size() : 0) + " args");
        System.out.println("\nscope=" + this.scope);


        ParseTreeWalker walker = new ParseTreeWalker();
        MethodExpressionListener listener = new MethodExpressionListener();
        walker.walk(listener, ctx.expression());
        
        
        String typeSpec = this.scope.resolveType(ctx.expression());
        System.out.println(ctx.expression().getText() + " isa : " + typeSpec);
    }

    enum Declaration {
        FIELD(FieldDeclarationFragment.class);

        final Class<? extends AbstractFragment> variableFragment;
        Declaration(Class<? extends AbstractFragment> variableFragment) {
            this.variableFragment = variableFragment;
        }
    }    
}
