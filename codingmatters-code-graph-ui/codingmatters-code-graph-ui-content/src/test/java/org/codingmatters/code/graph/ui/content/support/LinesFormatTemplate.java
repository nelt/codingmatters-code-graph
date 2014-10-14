package org.codingmatters.code.graph.ui.content.support;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by nel on 14/10/14.
 */
public class LinesFormatTemplate {

    @Test
    public void testSimpleFormat() throws Exception {
        assertThat(new Lines()
                .append("%hello%").format(Substitutions.replace("hello").with("world"))
                .content()
        ).isEqualTo("world");

    }
}
