package org.codingmatters.code.graph.bytecode.parser;

import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.bytecode.parser.exception.ClassParserException;
import org.codingmatters.code.graph.bytecode.parser.resolver.JarResolver;
import org.codingmatters.code.graph.cross.cutting.logs.Log;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 02/07/14
 * Time: 05:22
 * To change this template use File | Settings | File Templates.
 */
public class JarParser {

    static private final Log log = Log.log(JarParser.class);

    static public void parse(File jarFile, NodeProducer nodeProducer, PredicateProducer predicateProducer, String source) throws ClassParserException {
        try(JarResolver resolver = new JarResolver(jarFile);JarFile jar = new JarFile(jarFile)) {
            ClassParser parser = new ClassParser(nodeProducer, predicateProducer, resolver, source);
            new JarParser(jar, parser).parse();
        } catch (IOException e) {
            throw new ClassParserException("error parsing jar " + jarFile.getAbsolutePath(), e);
        }
    }
    
    private final ClassParser parser;
    private final JarFile jarFile;

    public JarParser(JarFile jarFile, ClassParser parser) {
        this.jarFile = jarFile;
        this.parser = parser;
    }
    
    public void parse() throws ClassParserException {
        Enumeration<JarEntry> entries = this.jarFile.entries();
        while(entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if(this.isClassEntry(entry)) {
                log.info("parsing jar entry %s", entry.getName());
                this.parser.parse(this.getClassName(entry));
            }
        }
    }

    private boolean isClassEntry(JarEntry entry) {
        return entry.getName().endsWith(".class");
    }

    private String getClassName(JarEntry entry) {
        return entry.getName().substring(0, entry.getName().length() - ".class".length());
    }
}
