package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.ui.content.elements.Navigation;
import org.codingmatters.code.graph.ui.content.elements.menu.DropdownMenu;
import org.codingmatters.code.graph.ui.content.elements.menu.LinkMenu;
import org.codingmatters.code.graph.ui.content.elements.menu.Menubar;
import org.codingmatters.code.graph.ui.content.support.Lines;

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

    static public Lines projectIndex() {
        return new Navigation()
                .withMenuBar(
                        new Menubar()
                                .withMenu(new LinkMenu("", "Home"))
                                .withMenu(new DropdownMenu()
                                        .withMenu(new LinkMenu("project 1/", "Project 1"))
                                        .withMenu(new LinkMenu("project 2/", "Project 2"))
                                        .withMenu(new LinkMenu("project 3/", "Project 3"))
                                )
                )
                .lines();
    }

}
