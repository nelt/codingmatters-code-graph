package org.codingmatters.code.graph.storage.neo4j.internal;

import org.codingmatters.code.graph.api.nodes.properties.annotations.Storable;
import org.codingmatters.code.graph.api.nodes.properties.annotations.StorableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 12/07/14
 * Time: 09:52
 * To change this template use File | Settings | File Templates.
 */
public class Neo4jPropertiesStorageProcessor {
    
    static private final Logger log = LoggerFactory.getLogger(Neo4jPropertiesStorageProcessor.class);
    

    public ToStore prepareStorage(String nodeName, Object data) {
        StringBuilder propertyMerger = new StringBuilder();
        Map<String, Object> properties = new HashMap<>();  
        
        boolean started = false;
        for (Method propertiesGetter : this.fileterAnnotatedMethods(data.getClass(), StorableProperties.class)) {
            Object propertiesHolder = this.invokeGetter(data, propertiesGetter);
            if(propertiesHolder != null) {
                for (Method storableGetter : this.fileterAnnotatedMethods(propertiesHolder.getClass(), Storable.class)) {
                    Object value = this.invokeGetter(propertiesHolder, storableGetter);
                    if(value != null) {
                        if(started) {
                            propertyMerger.append(", ");
                        } else {
                            started = true;
                        }
                        String propertyName = this.getFieldName(propertiesGetter) + "_" + this.getFieldName(storableGetter);
                        propertyMerger.append(nodeName).append(".").append(propertyName).append(" = {").append(propertyName).append("}");
                        properties.put(propertyName, value);
                    }
                }
            }
        }

        return new ToStore(propertyMerger.toString(), properties);
    }

    private String getFieldName(Method fromGetter) {
        if(fromGetter.getName().startsWith("get")) {
            return fromGetter.getName().substring("get".length(), "get".length() + 1).toLowerCase() + fromGetter.getName().substring("get".length() + 1);
        } else {
            return fromGetter.getName();
        }
    }

    private Object invokeGetter(Object o, Method propertiesGetter) {
        try {
            return propertiesGetter.invoke(o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("cannot invoke getter {} on {}", propertiesGetter, o);
            return null;
        }
    }

    private ArrayList<Method> fileterAnnotatedMethods(Class clazz, Class annotation) {
        ArrayList<Method> results = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if(method.getAnnotation(annotation) != null) {
                results.add(method);
            }
        }
        Collections.sort(results, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return results;
    }

    static public final ToStore NOTHING = new ToStore("", new HashMap<String,Object>());
    
    static public class ToStore {
        private final String propertyMergerString;
        private final Map<String, Object> parameters;

        public ToStore(String propertyMergerString, Map<String, Object> parameters) {
            this.propertyMergerString = propertyMergerString;
            this.parameters = parameters;
        }
        
        public String getPropertyMergerString() {
            return this.getPropertyMergerString(false);
        }
        
        public String getPropertyMergerString(boolean append) {
            if(append && this.propertyMergerString.length() > 0) {
                return ", " + this.propertyMergerString;
            } else {
                return propertyMergerString;
            }
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }
    } 
    
}
