package org.codingmatters.code.graph.test.support.text;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/09/14
 * Time: 21:55
 * To change this template use File | Settings | File Templates.
 */
public class TextFragmenter implements Iterator<String> {
    
    private String content;
    private final String startTag;
    private final String endTag;
    private String cleaningRegex;

    public TextFragmenter(String content, String startTag, String endTag) {
        this.content = content;
        this.startTag = startTag;
        this.endTag = endTag;
    }

    public TextFragmenter cleaning(String regex) {
        this.cleaningRegex = regex;
        return this;
    }
    
    @Override
    public boolean hasNext() {
        int start = this.content.indexOf(this.startTag);
        if(start == -1) return false;
        
        int end = this.content.indexOf(this.endTag, start + this.startTag.length());
        if(end == -1) return false;
        
        return true;
    }

    @Override
    public String next() {
        int start = this.content.indexOf(this.startTag);
        if(start == -1) throw new NoSuchFragmentException("start tag not found: " + this.startTag);

        int end = this.content.indexOf(this.endTag, start + this.startTag.length());
        if(end == -1) throw new NoSuchFragmentException("matching end tag not found after start at " + start + ": " + this.startTag);
        
        String result = this.content.substring(start + this.startTag.length(), end);
        if(result.startsWith("\n")) {
            result = result.substring(1);
        }
        if(result.endsWith("\n")) {
            result = result.substring(0, result.length() - 1);
        }
        
        this.content = this.content.substring(end + this.endTag.length());
        if(this.content.startsWith("\n")) {
            this.content = this.content.substring(1);
        }
        
        return this.clean(result);
    }

    private String clean(String result) {
        if(this.cleaningRegex == null) return result;
        
        return result.replaceAll(this.cleaningRegex, "");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
