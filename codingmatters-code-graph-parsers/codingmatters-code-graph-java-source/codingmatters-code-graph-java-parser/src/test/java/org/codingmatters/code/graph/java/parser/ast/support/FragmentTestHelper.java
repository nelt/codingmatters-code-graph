package org.codingmatters.code.graph.java.parser.ast.support;

import org.codingmatters.code.graph.java.parser.fragments.SourceFragment;
import org.junit.Assert;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.List;

import static org.codingmatters.code.graph.java.parser.JavaSource.fromResourceFile;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by nel on 25/02/15.
 */
public class FragmentTestHelper {
    
    private final Iterator<SourceFragment> fragmentIterator;
    private final List<SourceFragment> fragments;

    public FragmentTestHelper(List<SourceFragment> fragments) {
        this.fragments = fragments;
        this.fragmentIterator = fragments.iterator();
    }

    public void assertFragment(Class clazz, String qualifiedName) {
        SourceFragment actual = this.fragmentIterator.next();
        assertThat(actual).isOfAnyClassIn(clazz);
        assertThat(actual.qualifiedName()).isEqualTo(qualifiedName);
    }

    public void assertNoMoreFragment() {
        assertThat(this.fragmentIterator.hasNext()).isFalse();
    }

    public List<SourceFragment> getFragments() {
        return fragments;
    }


    public void assertAllFragmentAreCoherent(String resourceClass) throws Exception {
        for (SourceFragment fragment : this.getFragments()) {
            this.assertFragmentIsCoherent(fragment, resourceClass);
        }
    }

    private void assertFragmentIsCoherent(SourceFragment sourceFragment, String resource) throws Exception {
        StringBuilder fileContents = new StringBuilder();
        try (BufferedReader reader = ParsingTestHelper.buffered(fromResourceFile(resource))) {
            char[] buffer = new char[1024];
            for (int read = reader.read(buffer); read > -1; read = reader.read(buffer)) {
                fileContents.append(buffer, 0, read);
            }
        }

        String source = fileContents.toString();

        Assert.assertEquals(
                "expected " + sourceFragment.toString(), 
                source.substring(sourceFragment.start(), sourceFragment.end() + 1), 
                sourceFragment.text());
    }
}
