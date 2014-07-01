package org.codingmatters.code.graph.api.references;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 02/07/14
 * Time: 01:32
 * To change this template use File | Settings | File Templates.
 */
public class AbstractRefTest {

    @Test
    public void testOnlyShortName() throws Exception {
        TestRef actual = new TestRef("short");
        
        Assert.assertNull(actual.getSource());
        Assert.assertEquals("short", actual.getName());
        Assert.assertEquals("short", actual.getShortName());
        Assert.assertEquals("TestRef{name='short'}", actual.toString());
        Assert.assertEquals(new TestRef("short"), actual);
        Assert.assertNotEquals(new TestRef("source", "short"), actual);
        Assert.assertNotEquals(new TestRef("another"), actual);
    }
    
    @Test
    public void testSourceAndShortName() throws Exception {
        TestRef actual = new TestRef("source", "short");
        
        Assert.assertEquals("source", actual.getSource());
        Assert.assertEquals("source:short", actual.getName());
        Assert.assertEquals("short", actual.getShortName());
        Assert.assertEquals("TestRef{name='source:short'}", actual.toString());
        Assert.assertEquals(new TestRef("source", "short"), actual);
        Assert.assertNotEquals(new TestRef("short"), actual);
        Assert.assertNotEquals(new TestRef("another"), actual);
    }

    class TestRef extends AbstractRef{
        public TestRef(String shortName) {
            super(shortName);
        }
        public TestRef(String source, String shortName) {
            super(source, shortName);
        }
    }
}
