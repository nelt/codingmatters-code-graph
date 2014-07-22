package org.codingmatters.code.graph.java.parser;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 19/07/14
 * Time: 06:39
 * To change this template use File | Settings | File Templates.
 */
public class JavaSource {
    
    static public InputStream fromResourceFile(String classQualifiedName) {
        String resource = classQualifiedName.replace('.', '/') + ".java";
        return getResourceAsStream(resource);
    }
    
    static public InputStream fromResourceJar(String jarResource, String classQualifiedName) throws Exception {
        String entryName = classQualifiedName.replace('.', '/') + ".java";
        JarFile jar = new JarFile(new File(getResource(jarResource).toURI()));
        return jar.getInputStream(jar.getEntry(entryName));
    }

    private static URL getResource(String resource) {
        return Thread.currentThread().getContextClassLoader().getResource(resource);
    }


    private static InputStream getResourceAsStream(String resource) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    }

}
