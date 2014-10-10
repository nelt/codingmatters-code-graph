package org.codingmatters.code.graph.ui.content.elements.menu;

import org.codingmatters.code.graph.ui.content.elements.SearchBox;
import org.codingmatters.code.graph.ui.content.elements.UIElement;
import org.codingmatters.code.graph.ui.content.support.Lines;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 07/10/14
 * Time: 06:15
 * To change this template use File | Settings | File Templates.
 */
public class Menubar implements UIElement {
    
    static public final Menubar NONE = new Menubar() {
        @Override
        public Lines lines() {
            return new Lines();
        }
    };

    private final LinkedList<Menu> menus = new LinkedList<>();
    private SearchBox searchBox;
    
    public Menubar withMenu(Menu menu) {
        this.menus.add(menu);
        return this;
    }

    public Menubar withSearchBox(SearchBox searchBox) {
        this.searchBox = searchBox;
        return this;
    }
    
    public Lines lines() {
        Lines result = new Lines();
        result
                .appendLine("<div class=\"collapse navbar-collapse\">")
                .appendLine("    <ul class=\"nav navbar-nav navbar-left\">");
        for (Menu menu : this.menus) {
            result.append(menu.lines().prefix("        ").content());   
        }
        result.appendLine("    </ul>");
        if(this.searchBox != null) {
            result.appendLine(this.searchBox.lines().prefix("    ").content());
        }
        result.appendLine("</div>");

        return result;
    }
}
