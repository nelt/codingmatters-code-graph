package org.codingmatters.code.graph.ui.content.elements;

import org.codingmatters.code.graph.ui.content.elements.menu.Menubar;
import org.codingmatters.code.graph.ui.content.support.Lines;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 06/10/14
 * Time: 06:58
 * To change this template use File | Settings | File Templates.
 */
public class Navigation implements UIElement {
    
    private Menubar menubar = Menubar.NONE;

    public Navigation withMenuBar(Menubar menubar) {
        this.menubar = menubar != null ? menubar : Menubar.NONE;
        return this;
    }
    
    public Lines lines() {
        Lines result = new Lines()
                .appendLine("<div class=\"navbar navbar-inverse navbar-fixed-top\" role=\"navigation\">")
                .appendLine("    <div class=\"container\">")
                ;

        this.appendHeader(result);
        this.appendMenuBar(result);
        
        return result
                .appendLine("    </div>")
                .append("</div>")
                ;
    }



    private void appendHeader(Lines result) {
        result
                .appendLine("        <div class=\"navbar-header\">")
                .appendLine("            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">")
                .appendLine("                <span class=\"sr-only\">Toggle navigation</span>")
                .appendLine("                <span class=\"icon-bar\"></span>")
                .appendLine("                <span class=\"icon-bar\"></span>")
                .appendLine("                <span class=\"icon-bar\"></span>")
                .appendLine("            </button>")
                .appendLine("            <a class=\"navbar-brand\" href=\"https://github.com/nelt/codingmatters-code-graph\">Codingmatters' Code Graph</a>")
                .appendLine("        </div>")
                ;
    }

    private void appendMenuBar(Lines result) {
        result.append(this.menubar.lines().prefix("        ").content());
    }


}
