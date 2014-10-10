package org.codingmatters.code.graph.ui.service.api.project;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 07/10/14
 * Time: 21:55
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectService {
    List<Project> listProject();
    List<Version> listProjectVersions(Project project);
}
