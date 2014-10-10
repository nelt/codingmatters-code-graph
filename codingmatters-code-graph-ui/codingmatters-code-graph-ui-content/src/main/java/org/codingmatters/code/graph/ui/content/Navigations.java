package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.ui.content.elements.Navigation;
import org.codingmatters.code.graph.ui.content.elements.SearchBox;
import org.codingmatters.code.graph.ui.content.elements.menu.DropdownMenu;
import org.codingmatters.code.graph.ui.content.elements.menu.LinkMenu;
import org.codingmatters.code.graph.ui.content.elements.menu.Menu;
import org.codingmatters.code.graph.ui.content.elements.menu.Menubar;
import org.codingmatters.code.graph.ui.content.support.Lines;
import org.codingmatters.code.graph.ui.service.api.project.Project;
import org.codingmatters.code.graph.ui.service.api.project.ProjectService;
import org.codingmatters.code.graph.ui.service.api.project.Version;

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

    static public Lines project(Project current, ProjectService projectService) {
        return new Navigation()
                .withMenuBar(
                        new Menubar()
                                .withMenu(new LinkMenu("", "Home"))
                                .withMenu(projectsMenu(current, projectService))
                )
                .lines();
    }

    public static Lines version(Version version, ProjectService projectService) {
        return new Navigation()
                .withMenuBar(
                        new Menubar()
                                .withMenu(new LinkMenu("", "Home"))
                                .withMenu(projectsMenu(version.project(), projectService))
                                .withMenu(versionMenu(version, projectService))
                                .withSearchBox(versionSearchBox(version))
                )
                .lines();
    }

    private static Menu projectsMenu(Project current, ProjectService projectService) {
        DropdownMenu menu = new DropdownMenu(current.label());
        for (Project project : projectService.listProject()) {
            menu.withMenu(new LinkMenu(project.path(), project.label()));
        }
        return menu;
    }

    private static Menu versionMenu(Version current, ProjectService projectService) {
        DropdownMenu menu = new DropdownMenu(versionLabel(current));
        for (Version version : projectService.listProjectVersions(current.project())) {
            menu.withMenu(new LinkMenu(versionPath(version), versionLabel(version)));
        }
        menu.withDivider();
        menu.withMenu(new LinkMenu(current.project().path(), "All versions"));
        return menu;
    }

    private static String versionPath(Version version) {
        return version.project().path() + version.name() + "/";
    }

    private static String versionLabel(Version current) {
        return "Version " + current.name() + (current.latest() ? " (latest)" : "");
    }
    
    private static SearchBox versionSearchBox(Version version) {
        return new SearchBox(versionPath(version) + "search", "criteria", "Search...");
    }
}
