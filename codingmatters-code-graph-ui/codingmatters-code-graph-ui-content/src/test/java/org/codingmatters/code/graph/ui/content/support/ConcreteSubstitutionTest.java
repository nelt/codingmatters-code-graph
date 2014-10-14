package org.codingmatters.code.graph.ui.content.support;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ConcreteSubstitutionTest {

    @Test
    public void testOne() throws Exception {
        assertThat(new ConcreteSubstitution()
                .replace("hello").with("bonjour")
                        .in("%hello%")
        ).isEqualTo("bonjour");
    }
    
    @Test
    public void testOneCharBefore() throws Exception {
        assertThat(new ConcreteSubstitution()
                .replace("hello").with("bonjour")
                        .in("_%hello%")
        ).isEqualTo("_bonjour");
    }

    @Test
    public void testOneCharAfter() throws Exception {
        assertThat(new ConcreteSubstitution()
                        .replace("hello").with("bonjour")
                        .in("%hello%_")
        ).isEqualTo("bonjour_");
    }
    
    @Test
    public void testTwo() throws Exception {
        assertThat(new ConcreteSubstitution()
                .replace("hello").with("bonjour")
                .replace("world").with("le monde")
                        .in("%hello% %world%")
        ).isEqualTo("bonjour le monde");
    }
    
    @Test
    public void testOneTwice() throws Exception {
        assertThat(new ConcreteSubstitution()
                .replace("hello").with("bonjour")
                        .in("%hello% %hello%")
        ).isEqualTo("bonjour bonjour");
    }
    
}