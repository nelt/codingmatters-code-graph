package org.codingmatters.code.graph.ui.content.support;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 06/10/14
 * Time: 06:59
 * To change this template use File | Settings | File Templates.
 */
public class Lines {
    private StringBuilder content = new StringBuilder();

    public Lines append(String str) {
        this.content.append(str);
        return this;
    }

    public Lines appendLine(String str) {
        this.content.append(str + "\n");
        return this;
    }
    
    public String content() {
        return this.content.toString();
    }
}
