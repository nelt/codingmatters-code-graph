package org.codingmatters.code.graph.java.parser.internal;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.codingmatters.code.graph.java.ast.JavaLexer;
import org.codingmatters.code.graph.java.ast.JavaListener;
import org.codingmatters.code.graph.java.ast.JavaParser;
import org.codingmatters.code.graph.java.parser.fragments.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

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

    private ClassDisambiguizer disambiguizer;
    private HashSet<ClassDisambiguizerCall> disambiguizerCalls = new HashSet<>();
    private HashMap<ClassDisambiguizerCall, String> disambiguizerCallResults = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        this.fragments.clear();
        this.disambiguizerCalls.clear();

        this.disambiguizer = new ClassDisambiguizer() {
            @Override
            public String choosePackage(String forClass, String[] orderedCandidates) throws DisambiguizerException {
                if(Arrays.asList("Runnable", "String").contains(forClass)) {
                    return "java.lang";
                } else if("AtomicBoolean".equals(forClass)) {
                    return "java.util.concurrent.atomic";
                }

                ClassDisambiguizerCall disambiguizerCall = new ClassDisambiguizerCall(forClass, orderedCandidates);
                disambiguizerCalls.add(disambiguizerCall);
                System.out.println(disambiguizerCall + "->" + disambiguizerCallResults.get(disambiguizerCall));
                return disambiguizerCallResults.get(disambiguizerCall);
            }
        };

        this.listener = new SourceFragmentGeneratorListener(new FragmentStream() {
            @Override
            public void fragment(SourceFragment fragment) {
                fragments.add(fragment);
            }
        }, this.disambiguizer);
    }

    @Test
    public void testParse() throws Exception {
        String resourceClass = "org.codingmatters.code.graph.bytecode.parser.util.TestClass";
        this.parseResourceWith(resourceClass, this.listener);

        FragmentsAssertions fragmentsAssertions = new FragmentsAssertions(this.fragments);


        for (SourceFragment fragment : this.fragments) {
            System.out.println(fragment);
        }

        fragmentsAssertions.assertFragment(
                PackageFragment.class, "org.codingmatters.code.graph.bytecode.parser.util");
        fragmentsAssertions.assertFragment(
                ImportFragment.class, "java.util.Date");
        fragmentsAssertions.assertFragment(
                ImportFragment.class, "java.util.concurrent.atomic");
        fragmentsAssertions.assertFragment(
                ClassDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass");
        fragmentsAssertions.assertFragment(
                ClassDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass$InnerStaticClass");
        fragmentsAssertions.assertFragment(
                ClassDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass$InnerStaticClass$InnerInner");
        fragmentsAssertions.assertFragment(
                ClassUsageFragment.class, "java.lang.Runnable");
        fragmentsAssertions.assertFragment(
                ClassUsageFragment.class, "java.lang.String");
        fragmentsAssertions.assertFragment(
                ClassUsageFragment.class, "java.util.concurrent.atomic.AtomicBoolean");

        fragmentsAssertions.assertNoMoreFragment();

        for (SourceFragment fragment : this.fragments) {
            this.assertFragmentCoherent(fragment, resourceClass);    
        }
    }

    static public class FragmentsAssertions {
        private final Iterator<SourceFragment> fragments;

        public FragmentsAssertions(List<SourceFragment> fragments) {
            this.fragments = fragments.iterator();
        }

        public void assertFragment(Class clazz, String qualifiedName) {
            SourceFragment actual = this.fragments.next();
            assertThat(actual).isOfAnyClassIn(clazz);
            assertThat(actual.qualifiedName()).isEqualTo(qualifiedName);
        }

        public void assertNoMoreFragment() {
            assertThat(this.fragments.hasNext()).isFalse();
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

    static public BufferedReader buffered(InputStream stream) {
        return new BufferedReader(new InputStreamReader(stream));
    }

}
