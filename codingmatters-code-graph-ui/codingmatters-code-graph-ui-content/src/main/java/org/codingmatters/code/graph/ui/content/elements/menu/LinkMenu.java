package org.codingmatters.code.graph.ui.content.elements.menu;

import org.codingmatters.code.graph.ui.content.support.Lines;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 07/10/14
 * Time: 06:52
 * To change this template use File | Settings | File Templates.
 */
public class LinkMenu implements Menu {
    
    private final String link;
    private final String text;

    public LinkMenu(String link, String text) {
        this.link = link;
        this.text = text;
    }

    @Override
    public Lines lines() {
        Lines result = new Lines()
                .append("<li><a href=\"").append(this.link).append("\">").append(this.text).append("</a></li>")
                .appendLine("");
        return result;
    }
}
