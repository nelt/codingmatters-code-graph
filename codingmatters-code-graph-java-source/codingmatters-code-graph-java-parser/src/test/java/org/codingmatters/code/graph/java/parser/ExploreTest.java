package org.codingmatters.code.graph.java.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.codingmatters.code.graph.java.ast.JavaBaseListener;
import org.codingmatters.code.graph.java.ast.JavaLexer;
import org.codingmatters.code.graph.java.ast.JavaListener;
import org.codingmatters.code.graph.java.ast.JavaParser;
import org.codingmatters.code.graph.java.parser.fragments.AbstractFragment;
import org.codingmatters.code.graph.java.parser.fragments.PackageFragment;
import org.codingmatters.code.graph.java.parser.fragments.SourceFragment;
import org.fest.assertions.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static org.codingmatters.code.graph.java.parser.JavaSource.fromResourceFile;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 19/07/14
 * Time: 05:57
 * To change this template use File | Settings | File Templates.
 */
public class ExploreTest {
    
    private JavaListener listener;
    private List<SourceFragment> fragments = new LinkedList<>();
    
    @Before
    public void setUp() throws Exception {
        this.fragments.clear();
        this.listener = new JavaBaseListener() {
            private final Stack<String> namingStack = new Stack<>();
            private boolean inClass = false;

            @Override
            public void enterCompilationUnit(@NotNull JavaParser.CompilationUnitContext ctx) {
                this.namingStack.push(ctx.packageDeclaration().qualifiedName().getText());
                System.out.println(String.format("PACKAGE %s [%s, %s]",
                        ctx.packageDeclaration().qualifiedName().getText(),
                        ctx.packageDeclaration().qualifiedName().Identifier(0).getSymbol().getLine(),
                        ctx.packageDeclaration().qualifiedName().Identifier().get(0).getSymbol().getCharPositionInLine()
                ));
                
                try {
                    fragments.add(new AbstractFragment.Builder()
                            .withText(ctx.packageDeclaration().qualifiedName().getText())
                            .withStart(ctx.packageDeclaration().qualifiedName().Identifier().get(0).getSymbol().getStartIndex())
                            .withEnd(ctx.packageDeclaration().qualifiedName().Identifier().get(ctx.packageDeclaration().qualifiedName().Identifier().size() - 1).getSymbol().getStopIndex())
                            .build(PackageFragment.class));
                } catch (AbstractFragment.Builder.BuilderException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                super.enterCompilationUnit(ctx);
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

                this.namingStack.push(this.namingStack.peek() + (this.inClass ? "$" : ".") + ctx.Identifier().getText());
                this.inClass = true;
                super.enterClassDeclaration(ctx);
            }

            @Override
            public void exitClassDeclaration(@NotNull JavaParser.ClassDeclarationContext ctx) {
                super.exitClassDeclaration(ctx);
                this.inClass = false;
                this.namingStack.pop();
            }
        };
    }

    @Test
    public void testParse() throws Exception {
        JavaLexer lexer = new JavaLexer(new ANTLRInputStream(buffered(fromResourceFile("org.codingmatters.code.graph.bytecode.parser.util.TestClass"))));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);

        ParseTreeWalker walker = new ParseTreeWalker();
        
        walker.walk(this.listener, parser.compilationUnit());
        System.out.println(this.fragments);
        
        this.assertFragmentCoherent(this.fragments.get(0), "org.codingmatters.code.graph.bytecode.parser.util.TestClass");
    }

    private void assertFragmentCoherent(SourceFragment sourceFragment, String resource) throws IOException {
        StringBuilder fileContents = new StringBuilder();
        try(BufferedReader reader = buffered(fromResourceFile(resource));) {
            char[] buffer = new char[1024];
            for(int read = reader.read(buffer); read > -1; read = reader.read(buffer)) {
                fileContents.append(buffer, 0, read);
            }
        }
        
        String source = fileContents.toString();
        
        Assert.assertEquals(source.substring(sourceFragment.start(), sourceFragment.end() + 1), sourceFragment.text());
    }

    @Test
    public void testSourceFromTest() throws Exception {

        try(BufferedReader source = buffered(fromResourceFile("org.codingmatters.code.graph.bytecode.parser.util.TestClass"));) {
            for(String line = source.readLine() ; line != null ; line = source.readLine()) {
                System.out.println(line);
            }
        }
    }

    @Test
    public void testJar() throws Exception {
        try(BufferedReader source = buffered(JavaSource.fromResourceJar("junit-4.11-sources.jar", "junit.runner.BaseTestRunner"));) {
            for(String line = source.readLine() ; line != null ; line = source.readLine()) {
                System.out.println(line);
            }
        }
    }
    
    static public BufferedReader buffered(InputStream stream) {
        return new BufferedReader(new InputStreamReader(stream));
    }
}
