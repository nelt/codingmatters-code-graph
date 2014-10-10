package org.codingmatters.code.graph.ui.content.elements;

import org.codingmatters.code.graph.ui.content.support.Lines;

/**
 * Created by nel on 10/10/14.
 */
public class SearchBox implements UIElement {
    
    static public SearchBox NONE = new SearchBox(null, null, null) {
        @Override
        public Lines lines() {
            return new Lines();
        }
    };
    
    private final String action;
    private final String inputName;
    private final String placeholder;

    public SearchBox(String action, String inputName, String placeholder) {
        this.action = action;
        this.inputName = inputName;
        this.placeholder = placeholder;
    }

    @Override
    public Lines lines() {
        return new Lines().append(
                "<form class=\"navbar-form navbar-right\" role=\"search\" action=\"" + this.action + "\">\n" +
                "    <div class=\"input-group\">\n" +
                "        <input type=\"text\" name=\"" + this.inputName + "\" class=\"form-control\" placeholder=\"" + this.placeholder + "\"/>\n" +
                "        <span class=\"input-group-btn\">\n" +
                "            <button class=\"btn btn-default\" type=\"submit\">\n" +
                "                <span class=\"glyphicon glyphicon-search\"></span>\n" +
                "            </button>\n" +
                "        </span>\n" +
                "    </div>\n" +
                "</form>");
    }
}
