package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.ui.content.support.Lines;
import org.codingmatters.code.graph.ui.service.api.project.Project;
import org.codingmatters.code.graph.ui.service.api.project.ProjectService;

import java.util.List;


/**
 * Created by nel on 08/10/14.
 */
public class Projects {

    public static final int DIGEST_LIST_WIDTH = 3;

    static public Lines digest(Project project) {
        return new Lines()
                .appendLine("<a class=\"pull-left\" href=\"" + project.path() + "\">")
                .appendLine("    <img class=\"media-object img-circle\" src=\"data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==\" style=\"width: 100px; height: 100px;\" alt=\"Project Icon\">")
                .appendLine("</a>")
                .appendLine("<div class=\"media-object\">")
                .appendLine("    <h4><a href=\"" + project.path() + "\">" + project.label() + "</a></h4>")
                .appendLine("    <p>" + project.description() + "</p>")
                .append("</div>");
    }
    
    

    public static Lines digestList(ProjectService projectService) {
        Lines result = new Lines();

        List<Project> projects = projectService.listProject();
        for(int line = 0 ; line <= projects.size() / DIGEST_LIST_WIDTH; line++) {
            result.appendLine("<div class=\"row\">");
            for(int i = line * DIGEST_LIST_WIDTH; i < (line + 1) * DIGEST_LIST_WIDTH && i < projects.size() ; i++) {
                result
                        .appendLine("    <div class=\"col-lg-4 media\">")
                        .appendLine(digest(projects.get(i)).prefix("        ").content())
                        .appendLine("    </div>")
                ;
            }
            if(line < projects.size() / DIGEST_LIST_WIDTH) {
                result.appendLine("</div>");
            } else {
                result.append("</div>");
            }
        }
        return result;
    }

    public static Lines header(Project project) {
        return new Lines()
                .appendLine("<div class=\"row page-heading\">")
                .appendLine("    <div class=\"col-lg-3\">")
                .appendLine("        <a class=\"pull-left\" href=\"" + project.path() + "\">")
                .appendLine("            <img class=\"media-object img-circle\" src=\"data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==\" style=\"width: 100px; height: 100px;\" alt=\"Project Icon\">")
                .appendLine("        </a>")
                .appendLine("        <h1>" + project.label() + "</h1>")
                .appendLine("    </div>")
                .appendLine("    <div class=\"col-lg-4\">")
                .appendLine("        <p>" + project.description() + "</p>")
                .appendLine("    </div>")
                .appendLine("    <div class=\"col-lg-5\">")
                .appendLine("    </div>")
                .append("</div>");
    }
}
