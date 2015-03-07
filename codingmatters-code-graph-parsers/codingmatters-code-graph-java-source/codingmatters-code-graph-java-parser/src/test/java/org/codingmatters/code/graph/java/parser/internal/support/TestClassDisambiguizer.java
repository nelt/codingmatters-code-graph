package org.codingmatters.code.graph.java.parser.internal.support;

import org.codingmatters.code.graph.java.parser.internal.ClassDisambiguizer;
import org.codingmatters.code.graph.java.parser.internal.DisambiguizerException;
import org.codingmatters.code.graph.java.parser.internal.SourceFragmentGeneratorListenerTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nel on 25/02/15.
 */
public class TestClassDisambiguizer implements ClassDisambiguizer {

    static public WithPackage builder() {
        return new Builder();
    }

    public interface WithPackage {
        ForClass withPackage(String pack);
        TestClassDisambiguizer build();
    }

    public interface ForClass {
        WithPackage forClass(String... names);
    }

    static private class Builder implements WithPackage, ForClass {
        private final HashMap<List<String>, String> packagesForClass = new HashMap<>();
        private String nextPackage;

        @Override
        public WithPackage forClass(String... names) {
            if (names != null && names.length > 0) {
                this.packagesForClass.put(Arrays.asList(names), this.nextPackage);
            }
            return this;
        }

        @Override
        public ForClass withPackage(String pack) {
            this.nextPackage = pack;
            return this;
        }

        @Override
        public TestClassDisambiguizer build() {
            return new TestClassDisambiguizer(this.packagesForClass);
        }
    }

    private final HashMap<List<String>, String> packagesForClass;

    private TestClassDisambiguizer(HashMap<List<String>, String> packagesForClass) {
        this.packagesForClass = packagesForClass;
    }
    
    @Override
    public String choosePackage(String forClass, String[] orderedCandidates) throws DisambiguizerException {
        List<String> candidates = Arrays.asList(orderedCandidates);
        for (String candidate : candidates) {
            if(candidate.endsWith("." + forClass)) {
                return candidate.substring(0, candidate.length() - ("." + forClass).length());
            }
        }


        int packageIndex = -1;
        for (List<String> classes : this.packagesForClass.keySet()) {
            if (classes.contains(forClass)) {
                String p = this.packagesForClass.get(classes);
                int index = candidates.indexOf(p);
                if (index > packageIndex) {
                    packageIndex = index;
                }
            }
        }
        if (packageIndex != -1) {
            return candidates.get(packageIndex);
        } else {
            throw new DisambiguizerException("unknown class " + forClass);
        }
    }
}
