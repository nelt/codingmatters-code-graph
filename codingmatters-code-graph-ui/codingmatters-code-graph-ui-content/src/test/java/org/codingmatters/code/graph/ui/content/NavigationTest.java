package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.test.support.text.TextFile;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 06/10/14
 * Time: 07:02
 * To change this template use File | Settings | File Templates.
 */
public class NavigationTest {
    
    @Test
    public void testIndex() throws Exception {
        assertThat(Navigation.index().content())
                .isEqualTo(TextFile.read("index.html").htmlFragmenter("navigation bar").next());
    }
}
