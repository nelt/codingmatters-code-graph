package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.ui.content.elements.Navigation;
import org.codingmatters.code.graph.ui.content.elements.menu.DropdownMenu;
import org.codingmatters.code.graph.ui.content.elements.menu.LinkMenu;
import org.codingmatters.code.graph.ui.content.elements.menu.Menubar;
import org.codingmatters.code.graph.ui.content.support.Lines;
import org.codingmatters.code.graph.ui.service.api.project.Project;
import org.codingmatters.code.graph.ui.service.api.project.ProjectService;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 07/10/14
 * Time: 07:06
 * To change this template use File | Settings | File Templates.
 */
public class Navigations {

    static public Lines index() {
        return new Navigation().lines();
    }

    static public Lines projectIndex(Project current, ProjectService projectService) {
        DropdownMenu projectsMenu = new DropdownMenu(current.label());
        for (Project project : projectService.listProject()) {
            projectsMenu.withMenu(new LinkMenu(project.path(), project.label()));
        }
        return new Navigation()
                .withMenuBar(
                        new Menubar()
                                .withMenu(new LinkMenu("", "Home"))
                                .withMenu(projectsMenu)
                )
                .lines();
    }

}
