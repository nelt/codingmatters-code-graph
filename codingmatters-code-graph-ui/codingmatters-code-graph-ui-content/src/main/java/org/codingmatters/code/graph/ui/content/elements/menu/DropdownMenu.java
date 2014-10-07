package org.codingmatters.code.graph.ui.content.elements.menu;

import org.codingmatters.code.graph.ui.content.support.Lines;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 07/10/14
 * Time: 06:59
 * To change this template use File | Settings | File Templates.
 */
public class DropdownMenu implements Menu {


    private final String text;
    private final LinkedList<LinkMenu> menus = new LinkedList<>();

    public DropdownMenu(String text) {
        this.text = text;
    }

    public DropdownMenu withMenu(LinkMenu menu) {
        this.menus.add(menu);
        return this;
    }
    
    @Override
    public Lines lines() {
        Lines result = new Lines();
        result
                .appendLine("<li class=\"dropdown active\">")
                .appendLine("    <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">" + this.text + " <span class=\"caret\"></span></a>")
                .appendLine("    <ul class=\"dropdown-menu\" role=\"menu\">");

        for (Menu menu : this.menus) {
            result.append(menu.lines().prefix("        ").content());    
        }
        
        result
                .appendLine("    </ul>")
                .appendLine("</li>");
        return result;
    }
}
