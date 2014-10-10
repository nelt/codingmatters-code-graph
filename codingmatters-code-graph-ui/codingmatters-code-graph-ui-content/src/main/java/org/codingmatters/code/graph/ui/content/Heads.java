package org.codingmatters.code.graph.ui.content;

import org.codingmatters.code.graph.ui.content.elements.Head;
import org.codingmatters.code.graph.ui.content.support.Lines;
import org.codingmatters.code.graph.ui.service.api.project.*;

/**
 * Created by nel on 10/10/14.
 */
public class Heads {
    
    public static Lines head(String baseHref) {
        return new Head(null, baseHref).lines();
    }
    
    static public Lines head(Project project, String baseHref) {
        return new Head(project.label(), baseHref).lines();
    }
    
    static public Lines head(Version version, String baseHref) {
        return new Head(
                version.project().label() + " / Version " + version.name(), 
                baseHref
        ).lines();
    }
    
    static public Lines head(Pack pack, String baseHref) {
        return new Head(
                pack.version().project().label() + " / Version " + pack.version().name() + " / " + pack.name(), 
                baseHref
        ).lines();
    }
    
}
