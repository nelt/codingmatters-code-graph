package org.codingmatters.code.graph.bytecode.parser.asm;

import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 23/06/14
 * Time: 21:29
 * To change this template use File | Settings | File Templates.
 */
public class NameUtil {
    static public String methodName(String className, String rawMethodName, String methodDesc) {
        return new StringBuilder(className)
                .append("#").append(rawMethodName)
                .append(methodDesc).toString();
    }

    static public String fieldName(String className, String rawFieldName) {
        return className + "#" + rawFieldName;
    }

}
