package org.codingmatters.code.graph.bytecode.parser.functional;

import org.codingmatters.code.graph.api.references.Ref;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

/**
* Created with IntelliJ IDEA.
* User: nel
* Date: 11/07/14
* Time: 06:18
* To change this template use File | Settings | File Templates.
*/
class ContainsSourcePrefixOnlyOnce extends TypeSafeMatcher<Ref> {
    private final String expectedSourcePrefix;

    public ContainsSourcePrefixOnlyOnce(String expectedSourcePrefix) {
        this.expectedSourcePrefix = expectedSourcePrefix;
    }

    static public ContainsSourcePrefixOnlyOnce containsSourcePrefixOnlyOnce(String expectedSourcePrefix) {
        return new ContainsSourcePrefixOnlyOnce(expectedSourcePrefix);
    }

    @Override
    protected boolean matchesSafely(Ref ref) {
        return 
                startsWith(this.expectedSourcePrefix + ":").matches(ref.getName()) &&
                not(containsString(":")).matches(ref.getName().substring(this.expectedSourcePrefix.length() + 1));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("reference with source prefix ").appendText(this.expectedSourcePrefix).appendText(" only once");
    }
}
