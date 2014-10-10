package org.codingmatters.code.graph.ui.content.elements;

import org.codingmatters.code.graph.ui.content.support.Lines;

/**
 * Created by nel on 10/10/14.
 */
public class Head {
    
    private final String title;
    private final String baseHref;

    public Head(String title, String baseHref) {
        if(title != null) {
            this.title = "CodeGraph: " + title;
        } else {
            this.title = "CodeGraph";
        }
        this.baseHref = baseHref;
    }

    public Lines lines() {
        return new Lines()
                .append("<head>\n" +
                        "    <meta charset=\"utf-8\">\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                        "    \n" +
                        "    <base href=\"" + this.baseHref + "\" target=\"_self\">\n" +
                        "    \n" +
                        "    <title>" + this.title + "</title>\n" +
                        "    \n" +
                        "    <!-- Latest compiled and minified CSS -->\n" +
                        "    <link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\">\n" +
                        "    <link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css\">\n" +
                        "    \n" +
                        "    <link rel=\"stylesheet\" href=\"style/code-graph.css\">\n" +
                        "    \n" +
                        "    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->\n" +
                        "    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->\n" +
                        "    <!--[if lt IE 9]>\n" +
                        "    <script src=\"https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js\"></script>\n" +
                        "    <script src=\"https://oss.maxcdn.com/respond/1.4.2/respond.min.js\"></script>\n" +
                        "    <![endif]-->\n" +
                        "</head>");
    }
}