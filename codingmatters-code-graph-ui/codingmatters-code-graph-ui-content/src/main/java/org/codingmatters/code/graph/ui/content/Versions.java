package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.ui.content.support.Lines;
import org.codingmatters.code.graph.ui.service.api.project.Version;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by nel on 12/10/14.
 */
public class Versions {
    
    static public Lines digest(Version version) {
        return new Lines()
                .appendLine("<div class=\"row\">")
                .appendLine("    <div class=\"col-lg-1\"></div>")
                .appendLine("    <div class=\"col-lg-2\">")
                .appendLine("        <h4><a href=\"" + versionPath(version) + "\">" + versionLabel(version) + "</a></h4>")
                .appendLine("    </div>")
                .appendLine("    <div class=\"col-lg-5\">")
                .appendLine("        <p>" + version.description() + "</p>")
                .appendLine("    </div>")
                .appendLine("    <div class=\"col-lg-2\">")
                .appendLine("        <p>" + versionFormattedDate(version) + "</p>")
                .appendLine("    </div>")
                .append("</div>");
    }
    
    static private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    static public String versionFormattedDate(Version version) {
        return version.date() != null ? dateFormat.format(version.date()) : "";
    }

    static public String versionPath(Version version) {
        return version.project().path() + version.name() + "/";
    }

    static public String versionLabel(Version current) {
        return "Version " + current.name() + (current.latest() ? " (latest)" : "");
    }
}
