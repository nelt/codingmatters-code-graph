package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.ui.content.support.Lines;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 06/10/14
 * Time: 06:58
 * To change this template use File | Settings | File Templates.
 */
public class Navigation {
    static public Lines index() {
        return new Lines()
                .appendLine("<div class=\"navbar navbar-inverse navbar-fixed-top\" role=\"navigation\">")
                .appendLine("    <div class=\"container\">")
                .appendLine("        <div class=\"navbar-header\">")
                .appendLine("            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">")
                .appendLine("                <span class=\"sr-only\">Toggle navigation</span>")
                .appendLine("                <span class=\"icon-bar\"></span>")
                .appendLine("                <span class=\"icon-bar\"></span>")
                .appendLine("                <span class=\"icon-bar\"></span>")
                .appendLine("            </button>")
                .appendLine("            <a class=\"navbar-brand\" href=\"https://github.com/nelt/codingmatters-code-graph\">Codingmatters' Code Graph</a>")
                .appendLine("        </div>")
                .appendLine("    </div>")
                .append("</div>")
                ;
    }
}
