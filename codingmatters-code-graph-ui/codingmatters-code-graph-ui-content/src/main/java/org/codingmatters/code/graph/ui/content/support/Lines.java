package org.codingmatters.code.graph.ui.content.support;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 06/10/14
 * Time: 06:59
 * To change this template use File | Settings | File Templates.
 */
public class Lines {
    private StringBuilder content = new StringBuilder();

    public Lines append(String str) {
        this.content.append(str);
        return this;
    }

    public Lines appendLine(String str) {
        this.content.append(str + "\n");
        return this;
    }
    
    public String content() {
        return this.content.toString();
    }

    public Lines prefix(String prefix) {
        if(this.content.length() == 0) return this;

        List<Integer> lineMarkers = this.listNewLineIndices();
        Collections.reverse(lineMarkers);

        for (Integer lineMarker : lineMarkers) {
            this.content.replace(lineMarker + 1, lineMarker + 1, prefix);
        }
        this.content.replace(0, 0, prefix);
        
        return this;
    }

    private List<Integer> listNewLineIndices() {
        List<Integer> lineMarkers = new LinkedList<Integer>();
        for(int newLine = this.content.indexOf("\n"); newLine > -1 ; newLine = this.content.indexOf("\n", newLine + 1)) {
            if(newLine < this.content.length() - 1) {
                lineMarkers.add(newLine);
            }
        }
        return lineMarkers;
    }
}
