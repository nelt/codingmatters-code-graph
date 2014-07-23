package org.codingmatters.code.graph.java.parser.internal;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.codingmatters.code.graph.java.ast.JavaLexer;
import org.codingmatters.code.graph.java.ast.JavaListener;
import org.codingmatters.code.graph.java.ast.JavaParser;
import org.codingmatters.code.graph.java.parser.JavaSource;
import org.codingmatters.code.graph.java.parser.fragments.*;
import org.codingmatters.code.graph.java.parser.internal.SourceFragmentGeneratorListener;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.internal.Conditions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.codingmatters.code.graph.java.parser.JavaSource.fromResourceFile;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 19/07/14
 * Time: 05:57
 * To change this template use File | Settings | File Templates.
 */
public class SourceFragmentGeneratorListenerTest {
    
    private JavaListener listener;
    private List<SourceFragment> fragments = new LinkedList<>();
    
    @Before
    public void setUp() throws Exception {
        this.fragments.clear();
        this.listener = new SourceFragmentGeneratorListener(new FragmentStream() {
            @Override
            public void fragment(SourceFragment fragment) {
                fragments.add(fragment);
            }
        });
    }

    @Test
    public void testParse() throws Exception {
        String resourceClass = "org.codingmatters.code.graph.bytecode.parser.util.TestClass";
        this.parseResourceWith(resourceClass, this.listener);

        for (SourceFragment fragment : this.fragments) {
            System.out.println(fragment);
        }


        Iterator<SourceFragment> actualFragment = this.fragments.iterator();
        
        this.assertFragment(PackageFragment.class, "org.codingmatters.code.graph.bytecode.parser.util", actualFragment.next());
        this.assertFragment(ImportFragment.class, "java.util.Date", actualFragment.next());
        this.assertFragment(ClassDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass", actualFragment.next());
        this.assertFragment(ClassDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass$InnerStaticClass", actualFragment.next());
        
        assertFalse(actualFragment.hasNext());
        
        for (SourceFragment fragment : this.fragments) {
            this.assertFragmentCoherent(fragment, resourceClass);    
        }
    }

    private void assertFragment(Class clazz, String qualifiedName, SourceFragment actual) {
        assertThat(actual).isOfAnyClassIn(clazz);
        assertThat(actual.qualifiedName()).isEqualTo(qualifiedName);
    }

    private void parseResourceWith(String resource, JavaListener lst) throws IOException {
        this.parseWith(fromResourceFile(resource), lst);
    }

    private void parseWith(InputStream stream, JavaListener lst) throws IOException {
        JavaLexer lexer = new JavaLexer(new ANTLRInputStream(buffered(stream)));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(lst, parser.compilationUnit());
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
        
        Assert.assertEquals("expected " + sourceFragment.toString(), source.substring(sourceFragment.start(), sourceFragment.end() + 1), sourceFragment.text());
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
