package org.codingmatters.code.graph.api.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 04/07/14
 * Time: 05:38
 * To change this template use File | Settings | File Templates.
 */
public class ClassBean {
    private String ref;
    private List<FieldBean> fields;
    private List<MethodBean> methods;

    public ClassBean() {
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    public List<FieldBean> getFields() {
        return fields;
    }

    public void setFields(List<FieldBean> fields) {
        this.fields = fields;
    }

    public List<MethodBean> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodBean> methods) {
        this.methods = methods;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "ClassBean{" +
                "fields=" + fields +
                ", methods=" + methods +
                ", ref='" + ref + '\'' +
                '}';
    }
}
