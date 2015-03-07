package org.codingmatters.code.graph.bytecode.parser.util;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 02/07/14
 * Time: 00:45
 * To change this template use File | Settings | File Templates.
 */
public class ClassResourcesHelper {
    
    static public File getBytecodeRootFor(Class clazz) throws URISyntaxException {
        String classFilePath = getBytecodeFile(clazz).getAbsolutePath();
        return new File(classFilePath.substring(0, classFilePath.length() - getBytecodeRelativePath(clazz).length()));
    }
    
    static private File getBytecodeFile(Class clazz) throws URISyntaxException {
        return new File(getResource(getBytecodeRelativePath(clazz)).toURI());
    }

    private static String getBytecodeRelativePath(Class clazz) {
        return clazz.getName().replace('.', '/') + ".class";
    }

    static public File makeTemporaryJarFile(Class... classes) throws IOException {
        File result = File.createTempFile("temporary", ".jar");
        result.deleteOnExit();
        try (
                FileOutputStream fout = new FileOutputStream(result);
                JarOutputStream jout = new JarOutputStream(fout)
        ) {
            HashSet<String> directories = new HashSet<>();
            for (Class clazz : classes) {
                String directory = getBytecodeRelativePath(clazz).substring(0, getBytecodeRelativePath(clazz).lastIndexOf("/"));
                if(! directories.contains(directory)) {
                    jout.putNextEntry(new JarEntry(directory));
                    directories.add(directory);
                }
                jout.putNextEntry(new JarEntry(getBytecodeRelativePath(clazz)));
                jout.write(getBytes(clazz));
                jout.closeEntry();
            }
            return result;
        }
    }

    private static byte [] getBytes(Class clazz) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try(InputStream in = getResourceAsStrem(getBytecodeRelativePath(clazz))) {
            byte [] buffer = new byte[1024];
            for(int read = in.read(buffer) ; read > -1 ; read = in.read(buffer)) {
                result.write(buffer, 0, read);
            }
        }
        return result.toByteArray();
    }

    private static InputStream getResourceAsStrem(String name) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    }

    private static URL getResource(String name) {
        return Thread.currentThread().getContextClassLoader().getResource(name);
    }

}
