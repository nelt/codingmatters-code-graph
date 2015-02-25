package org.codingmatters.code.graph.java.parser.internal.support;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.codingmatters.code.graph.java.ast.JavaLexer;
import org.codingmatters.code.graph.java.ast.JavaListener;
import org.codingmatters.code.graph.java.ast.JavaParser;
import org.codingmatters.code.graph.java.parser.fragments.FragmentStream;
import org.codingmatters.code.graph.java.parser.fragments.SourceFragment;
import org.codingmatters.code.graph.java.parser.internal.ClassDisambiguizer;
import org.codingmatters.code.graph.java.parser.internal.SourceFragmentGeneratorListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import static org.codingmatters.code.graph.java.parser.JavaSource.fromResourceFile;

/**
 * Created by nel on 25/02/15.
 */
public class ParsingTestHelper {
    
    static public BufferedReader buffered(InputStream stream) {
        return new BufferedReader(new InputStreamReader(stream));
    }
    
    static public FragmentTestHelper parseResource(String resource, ClassDisambiguizer disambiguizer) throws IOException {
        final LinkedList<SourceFragment> fragments = new LinkedList<>();
        SourceFragmentGeneratorListener lst = new SourceFragmentGeneratorListener(new FragmentStream() {
            @Override
            public void fragment(SourceFragment fragment) {
                fragments.add(fragment);
            }
        }, disambiguizer);
        
        parseWith(fromResourceFile(resource), lst);
        
        return new FragmentTestHelper(fragments);
    }

    static private void parseWith(InputStream stream, JavaListener lst) throws IOException {
        JavaLexer lexer = new JavaLexer(new ANTLRInputStream(buffered(stream)));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(lst, parser.compilationUnit());
    }
}
