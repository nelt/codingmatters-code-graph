package org.codingmatters.code.graph.java.parser.internal;

import org.codingmatters.code.graph.java.parser.fragments.*;
import org.codingmatters.code.graph.java.parser.internal.support.FragmentTestHelper;
import org.codingmatters.code.graph.java.parser.internal.support.ParsingTestHelper;
import org.codingmatters.code.graph.java.parser.internal.support.TestClassDisambiguizer;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 19/07/14
 * Time: 05:57
 * To change this template use File | Settings | File Templates.
 */
public class SourceFragmentGeneratorListenerTest {
    
    private ClassDisambiguizer disambiguizer;

    @Before
    public void setUp() throws Exception {
        this.disambiguizer = TestClassDisambiguizer.builder()
                .withPackage("java.lang").forClass("Runnable", "String")
                .withPackage("java.util.concurrent.atomic").forClass("AtomicBoolean")
                .build();
    }
    
    @Test
    public void testParse() throws Exception {
        String resourceClass = "org.codingmatters.code.graph.bytecode.parser.util.TestClass";
        FragmentTestHelper parsedFragments = ParsingTestHelper.parseResource(resourceClass, this.disambiguizer);

        System.out.println("-----------FRAGMENTS-----------------");
        for (SourceFragment fragment : parsedFragments.getFragments()) {
            System.out.println(fragment);
        }
        System.out.println("-------------------------------------");
        
        
        /*
        package org.codingmatters.code.graph.bytecode.parser.util;

        import java.util.Date;
        import java.util.concurrent.atomic.*;
         */
        parsedFragments.assertFragment(
                PackageFragment.class, "org.codingmatters.code.graph.bytecode.parser.util");
        parsedFragments.assertFragment(
                ImportFragment.class, "java.util.Date");
        parsedFragments.assertFragment(
                ImportFragment.class, "java.util.concurrent.atomic");
        
        /*
        public class TestClass {
         */
        parsedFragments.assertFragment(
                ClassDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass");
        
        /*
        static public class InnerStaticClass {
           class InnerInner {
            }
        }
         */
        parsedFragments.assertFragment(
                ClassDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass$InnerStaticClass");
        parsedFragments.assertFragment(
                ClassDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass$InnerStaticClass$InnerInner");
        
        /*
        static private Runnable run = new Runnable() {
            @Override
            public void run() {
            }
        };
         */
        parsedFragments.assertFragment(
                ClassUsageFragment.class, "java.lang.Runnable");
        parsedFragments.assertFragment(
                FieldDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass#run");
        parsedFragments.assertFragment(
                MethodDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass$1#run()");


        /*
        private final String field;
        private AtomicBoolean dd;
         */
        parsedFragments.assertFragment(
                ClassUsageFragment.class, "java.lang.String");
        parsedFragments.assertFragment(
                FieldDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass#field");

        parsedFragments.assertFragment(
                ClassUsageFragment.class, "java.util.concurrent.atomic.AtomicBoolean");
        parsedFragments.assertFragment(
                FieldDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass#dd");
        
        /*
        public TestClass(String field) {
            this.field = field;
        }
         */
        parsedFragments.assertFragment(
                MethodDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass#<init>(Ljava/lang/String;)V");
        
        
        /*
        public String getField() {
            return field;
        }
         */
        parsedFragments.assertFragment(
                MethodDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass#getField()Ljava/lang/String;");
     
        /*
        public String method(String arg) throws Exception {
            Date d = new Date();
            return d.toString();
        }
         */
        parsedFragments.assertFragment(
                MethodDeclarationFragment.class, "org.codingmatters.code.graph.bytecode.parser.util.TestClass#method(Ljava/lang/String;)Ljava/lang/String;");


        parsedFragments.assertNoMoreFragment();

        parsedFragments.assertAllFragmentAreCoherent(resourceClass);
    }
}
